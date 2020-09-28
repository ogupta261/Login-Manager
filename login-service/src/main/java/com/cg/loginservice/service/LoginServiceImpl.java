package com.cg.loginservice.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import com.cg.loginservice.dao.LoginDAO;
import com.cg.loginservice.entity.LoginCredentials;
import com.cg.loginservice.dto.LoginCredentialsDTO;
import com.cg.loginservice.dto.LoginDTO;
import com.cg.loginservice.exceptions.CustomException;
import com.cg.loginservice.id.LoginCredentialsId;
import static com.cg.loginservice.utility.Verification.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

	private Logger log=LoggerFactory.getLogger(this.getClass());
	private String userOptionalEmptyError = "User not Found!";
	@Autowired
	LoginDAO loginDao;

	@Override
	public LoginDTO authenticateUser(String userName, String password, String role) {
		verifyUserName(userName);
		verifyPassword(password);
		verifyRole(role);
		LoginCredentials user=loginDao.validateUser(userName, password, role);
		if(user!=null){
			log.info("User Validated! - "+user.toString());
			LoginDTO result = new LoginDTO(user);
			user.setLastLoginDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			return result;
		}else{
			log.error("User Validation Failed!", CustomException.class);
			throw new CustomException("Credentials Invalid!");
		}
			
	}

	@Override
	public LoginDTO getUser(String userName, String role) {
		verifyUserName(userName);
		verifyRole(role);
		Optional<LoginCredentials> temp = loginDao.findById(new LoginCredentialsId(userName, role));
		if(temp.isEmpty()) throw new CustomException(userOptionalEmptyError);
		log.info("User Details returned for "+ userName+"!");
		return new LoginDTO(temp.get());
	}

	@Override
	public LoginDTO changeAuthStatus(String userName, String role) {
		verifyUserName(userName);
		verifyRole(role);
		Optional<LoginCredentials> cred = loginDao.findById(new LoginCredentialsId(userName, role));
		if(cred.isEmpty()) throw new CustomException(userOptionalEmptyError);
		LoginCredentials temp = cred.get();
		temp.setAuthConsent(!temp.getAuthConsent());
		log.info("AuthConsent Changed for "+ userName+"!");
		return new LoginDTO(loginDao.save(temp));
	}

	@Override
	public boolean changePassword(String userName, String role, String newPassword) {
		verifyUserName(userName);
		verifyRole(role);
		verifyPassword(newPassword);
		Optional<LoginCredentials> cred = loginDao.findById(new LoginCredentialsId(userName, role));
		if(cred.isEmpty()) throw new CustomException(userOptionalEmptyError);
		LoginCredentials temp = cred.get();
		temp.setPassword(newPassword);
		log.info("Password Changed for "+ userName+"!");
		loginDao.save(temp);
		return true;
	}

	@Override
	public LoginDTO addCredentials(LoginCredentialsDTO credentials) {
		verifyUserName(credentials.getUserName());
		verifyPassword(credentials.getPassword());
		verifyRole(credentials.getRole());
		verifyAuthConsentValid(credentials.getAuthConsent());
		if(!loginDao.existsById(new LoginCredentialsId(credentials.getUserName(), credentials.getRole()))) {	
			log.info("Adding credentials: "+credentials.getUserName()+" - " +credentials.getRole());
			credentials.setLastLoginDate(Date.from(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
			return new LoginDTO(loginDao.save(new LoginCredentials(credentials)));
		}else {
			log.error("User Exists!", CustomException.class);
			throw new CustomException("User Already Exists!");
		}
	}

	@Override
	public boolean deleteCredentials(String userName, String role) {
		verifyUserName(userName);
		verifyRole(role);
		if(loginDao.existsById(new LoginCredentialsId(userName, role))) {	
			log.info("Deleting credentials: "+userName+" - "+role);
			loginDao.deleteById(new LoginCredentialsId(userName, role));
			return true;
		}
		else {
			log.error(userOptionalEmptyError, CustomException.class);
			return false;
		}

	}

}
