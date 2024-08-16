package sl.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sl.project.models.LoginSignup;
import sl.project.repository.LoginSignupRepository;

@Service
public class LoginSignupService implements ILoginSignupService {
	
	@Autowired
	private LoginSignupRepository loginsignuprepository;
	
	@Override
	public LoginSignup savelogin( LoginSignup portfolio) {
		try {		    
		    
		    LoginSignup ls = loginsignuprepository.findByEmail(portfolio.getEmail());
		    
		    if(ls != null) {
		        return null;
		    } else {
		        LoginSignup savedlogin = loginsignuprepository.save(portfolio);
                if(savedlogin != null) {
                    return savedlogin;
                }
                else {
                    return null;
                }
		    }
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	
		return null;

	}

	@Override
	public boolean check(String email, String password) {
		LoginSignup loginSignup = loginsignuprepository.findByEmail(email);
		
		if(loginSignup != null) {
			if(password.equals(loginSignup.getPassword())) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	
}
