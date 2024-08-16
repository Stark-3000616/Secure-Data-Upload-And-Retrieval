package sl.project.services;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import sl.project.models.FileUploadDownload;
import sl.project.repository.FileUploadDownloadRepository;

@Service
public class FileUploadDownloadService implements IFileUploadDownloadService {

    @Autowired
    private FileUploadDownloadRepository fileUploadDownloadRepository;
    
    private final String initialKey = "hgyrixchdlpqweithv";
    private byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};  
    private static SecretKeySpec AESSecretKey;
    private static byte[] keyBytesArray;
    
    @Override
    public void getAESFinalKey(String initialKey) {
        try {
            // Reference : https://howtodoinjava.com/java/java-security/java-aes-encryption-example/
            MessageDigest md = null;
            keyBytesArray = initialKey.getBytes("UTF-8");
            md = MessageDigest.getInstance("SHA-1");
            keyBytesArray = md.digest(keyBytesArray);
            keyBytesArray = Arrays.copyOf(keyBytesArray, 16);
            AESSecretKey = new SecretKeySpec(keyBytesArray, "AES"); 
        } catch(Exception e) {
            e.printStackTrace();
        }   
    }

    @Override
    public String encryptData(String str) {
        try {
            String encryptedData;
            // Reference : https://howtodoinjava.com/java/java-security/java-aes-encryption-example/
            getAESFinalKey(initialKey);
            
            IvParameterSpec initializationVector = new IvParameterSpec(iv);                      
            Cipher cipherText = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipherText.init(Cipher.ENCRYPT_MODE, AESSecretKey, initializationVector);
            encryptedData = Base64.getEncoder().encodeToString(cipherText.doFinal(str.getBytes("UTF-8")));
            return encryptedData;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String pdfToText(MultipartFile file) {
        String str = "";
        try {
            PDDocument pdDoc = PDDocument.load(file.getInputStream());
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            str = pdfTextStripper.getText(pdDoc);
            return str;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }    
    
    @Override
    public FileUploadDownload upload(MultipartFile file) {
        String file_name = StringUtils.cleanPath(file.getOriginalFilename());
        String file_ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        
        try {
            String cipherFileName = encryptData(file_name);
            if(file_ext.equals("pdf")) {
                String textFile = pdfToText(file);
                String cipher = encryptData(textFile);
                FileUploadDownload fileUploadDownload = new FileUploadDownload(cipherFileName, cipher.getBytes());
                return fileUploadDownloadRepository.save(fileUploadDownload);
                
            } else if(file_ext.equals("txt") || file_ext.equals("sh")) {
                String body = new String(file.getBytes());
    //          System.out.println("BODY : "+body);
                String cipher = encryptData(body);
                FileUploadDownload fileUploadDownload = new FileUploadDownload(cipherFileName, cipher.getBytes());
                return fileUploadDownloadRepository.save(fileUploadDownload);
            } else {
                return null;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String decryptData(String str) {
        try {
            String decryptedData;
            
            // Reference : https://howtodoinjava.com/java/java-security/java-aes-encryption-example/
            getAESFinalKey(initialKey);
            
            IvParameterSpec initializationVector = new IvParameterSpec(iv);  
            Cipher plainText = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            plainText.init(Cipher.DECRYPT_MODE, AESSecretKey,initializationVector);
            decryptedData = new String(plainText.doFinal(Base64.getDecoder().decode(str)));
            return decryptedData;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public FileUploadDownload getFile(String str) {
        String cipherFileName = encryptData(str);
        
        FileUploadDownload fileUploadDownload = fileUploadDownloadRepository.findByName(cipherFileName);
        String cipher = new String(fileUploadDownload.getContent());
        String plainText = decryptData(cipher);
        fileUploadDownload.setContent(plainText.getBytes());    
        fileUploadDownload.setName(str);
        
        return fileUploadDownload;
    }

    @Override
    public ArrayList<FileUploadDownload> view() {
        List<FileUploadDownload> my_list = new ArrayList<>();
        my_list = fileUploadDownloadRepository.findAll();
        return (ArrayList<FileUploadDownload>) my_list;
    }

    @Override
    public void delete(String id) {
        fileUploadDownloadRepository.deleteById(id);
    }

}
