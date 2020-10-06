package com.cg.loginservice.authentication;

import java.util.List;
import java.util.Optional;

import com.cg.loginservice.dao.UserOTPDAO;
import com.cg.loginservice.dto.LoginDTO;
import com.cg.loginservice.dto.UserOTPDTO;
import com.cg.loginservice.entity.UserOTP;
import com.cg.loginservice.exceptions.CustomException;
import com.cg.loginservice.service.LoginService;
import com.warrenstrange.googleauth.ICredentialRepository;
import static com.cg.loginservice.utility.Verification.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CredentialRepository implements ICredentialRepository {

    private Logger log=LoggerFactory.getLogger(this.getClass());
    private String logUserNo2FA = "User not generated 2FA!";
    private String throwUserNo2FA = "User has not generated 2FA authentication.";
    @Autowired
    private UserOTPDAO userOTPDAO;

    @Autowired
    private LoginService loginService;

    @Override
    public String getSecretKey(String userNameCommaRole) {
        String[] userDetails = userNameCommaRole.split(",");
        LoginDTO user = loginService.getUser(userDetails[0], userDetails[1]);
        verifyAuthConsentEqualsTrue(user);
        Optional<UserOTP> temp = userOTPDAO.findById(userNameCommaRole);
        if(temp.isEmpty()) {
            log.error(logUserNo2FA, CustomException.class);
            throw new CustomException(throwUserNo2FA);
        }
        log.info("User Key Returned: "+userDetails[0]+" - "+userDetails[1]);
        return temp.get().getSecretKey();
    }

    @Override
    public void saveUserCredentials(String userNameCommaRole,
                                    String secretKey,
                                    int validationCode,
                                    List<Integer> scratchCodes) {
        String[] userDetails = userNameCommaRole.split(",");
        LoginDTO user = loginService.getUser(userDetails[0], userDetails[1]);
        verifyAuthConsentEqualsTrue(user);
        // Optional<UserOTP> temp = userOTPDAO.findById(userNameCommaRole);
        // if(temp.isPresent()) {
        //     log.error(logUser2FA, CustomException.class);
        //     throw new CustomException(throwUser2FA);
        // }
        log.info("User 2FA Created: "+userDetails[0]+" - "+userDetails[1]);
        userOTPDAO.save(new UserOTP(userNameCommaRole, secretKey, validationCode, scratchCodes));
    }

    public UserOTPDTO getUser(String userNameCommaRole) {
        String[] userDetails = userNameCommaRole.split(",");
        LoginDTO user = loginService.getUser(userDetails[0], userDetails[1]);
        verifyAuthConsentEqualsTrue(user);
        
        Optional<UserOTP> temp = userOTPDAO.findById(userNameCommaRole);
        if(temp.isEmpty()) {
            log.error(logUserNo2FA, CustomException.class);
            throw new CustomException(throwUserNo2FA);
        }
        UserOTP userAuth = temp.get();
        log.info("User Returned: "+userDetails[0]+" - "+userDetails[1]);
        return new UserOTPDTO(userNameCommaRole, userAuth.getSecretKey(), userAuth.getValidationCode(), userAuth.getScratchCodes());
    }

    public boolean removeUser(String userNameCommaRole) {
        String[] userDetails = userNameCommaRole.split(",");
        verifyUserName(userDetails[0]);
        verifyRole(userDetails[1]);
        Optional<UserOTP> user = userOTPDAO.findById(userNameCommaRole);
        if(user.isEmpty()) {
            log.error(logUserNo2FA, CustomException.class);
            throw new CustomException(throwUserNo2FA);
        }
        userOTPDAO.deleteById(userNameCommaRole);
        log.info("User("+userDetails[0]+"-"+userDetails[1]+") 2FA deleted.");
        return true;
    }
    
}
