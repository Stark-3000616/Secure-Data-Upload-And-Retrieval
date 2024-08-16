package sl.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sl.project.models.FileUploadDownload;

@Repository
public interface FileUploadDownloadRepository extends JpaRepository<FileUploadDownload, String> {
    
    FileUploadDownload findByName(String name);
    
}
