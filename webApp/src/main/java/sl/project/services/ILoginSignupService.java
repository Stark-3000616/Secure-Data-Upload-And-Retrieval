package sl.project.services;

import sl.project.models.LoginSignup;

public interface ILoginSignupService {

	public LoginSignup savelogin(LoginSignup portfolio);
	
	public boolean check(String email, String password);

}
