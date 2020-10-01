package com.cg.loginservice.utility;

import com.cg.loginservice.dto.LoginDTO;
import com.cg.loginservice.exceptions.CustomException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Verification {
    private Verification() {
        
    }
    private static Logger log = LoggerFactory.getLogger(Verification.class);
    public static void verifyUserName(String userName) {
        if (userName.length() == 0) {
            log.error("Username Empty!", CustomException.class);
            throw new CustomException("Username cannot be Empty!");
        }
    }
    public static void verifyPassword(String password) {
        if(password.length()==0){
			log.error("Password Empty!", CustomException.class);
			throw new CustomException("Password cannot be Empty!");
		}
		if(password.length()<6 || password.charAt(0)>'Z' || password.charAt(0)<'A'){
			log.error("Password Not Following Policy!", CustomException.class);
			throw new CustomException("Password not following Policy. Greater than 6 characters, Starting with Capital Letter.");
		}
    }
    public static void verifyRole(String role) {
        if(role.length()==0) {
            log.error("Role Empty!", CustomException.class);
            throw new CustomException("Role cannot be Empty!");
        }
    }
    public static void verifyAuthConsentEqualsTrue(LoginDTO user) {
        if(!user.getAuthConsent()) {
            log.error("User has no 2FA!", CustomException.class);
            throw new CustomException("User did not opt for 2FA. Connot generate QR Code.");
        }
    }
    public static void verifyAuthConsentValid(boolean authConsent) {
        if(authConsent!=true && authConsent!=false) {
			log.error("AuthConsent Empty!", CustomException.class); 
			throw new CustomException("AuthConsent cannot be Empty");
		}
    }
}
