package sl.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sl.project.models.FileUploadDownload;
import sl.project.models.LoginSignup;

@Repository
public interface LoginSignupRepository extends JpaRepository<LoginSignup, String> {
	
	LoginSignup findByEmail(String email);
	
}
