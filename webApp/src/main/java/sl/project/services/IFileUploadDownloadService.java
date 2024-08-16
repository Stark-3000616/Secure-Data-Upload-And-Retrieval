package sl.project.services;

import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import sl.project.models.FileUploadDownload;

public interface IFileUploadDownloadService {
    
    public FileUploadDownload upload(MultipartFile file);
        
    public void getAESFinalKey(String initialKey);
    
    public String encryptData(String str);
        
    public String pdfToText(MultipartFile file);
    
    public String decryptData(String str);
    
    public FileUploadDownload getFile(String str);
    
    public ArrayList<FileUploadDownload> view();
    
    public void delete(String id);
    
}