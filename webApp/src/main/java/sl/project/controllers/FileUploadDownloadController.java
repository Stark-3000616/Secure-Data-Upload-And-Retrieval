package sl.project.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import sl.project.models.FileUploadDownload;
import sl.project.services.FileUploadDownloadService;

@CrossOrigin
@RestController 
@RequestMapping("/api")
public class FileUploadDownloadController {
    
    @Autowired
    private FileUploadDownloadService fileUploadDownloadService;
    
    @CrossOrigin
    @PostMapping("/upload")
    public ResponseEntity<FileUploadDownload> uploadFile(@RequestParam("file") MultipartFile file) {
        
        FileUploadDownload fileUploadDownload = fileUploadDownloadService.upload(file);
        
        if(fileUploadDownload != null) {
            return new ResponseEntity<>(fileUploadDownload, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @CrossOrigin
    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestBody String str) {
        
        FileUploadDownload fileUploadDownload = fileUploadDownloadService.getFile(str);
        
        if(fileUploadDownload != null) {
            return new ResponseEntity<>(fileUploadDownload.getContent(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileUploadDownload.getName() + "\"")
//                .body(fileUploadDownload.getContent());
    }
    
    @CrossOrigin
    @GetMapping("/view")
    public ResponseEntity<ArrayList<FileUploadDownload>> view() {
        
        ArrayList<FileUploadDownload> my_list = new ArrayList<>();
        my_list = fileUploadDownloadService.view();
        
        if(my_list != null) {
            return new ResponseEntity<>(my_list, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @CrossOrigin
    @PostMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody String id) {
        
        fileUploadDownloadService.delete(id);
        
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
     
}
