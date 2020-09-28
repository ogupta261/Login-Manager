package com.cg.loginservice.controller;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.cg.loginservice.authentication.CredentialRepository;
import com.cg.loginservice.dto.RegistrationDataDTO;
import com.cg.loginservice.dto.ValidateCodeDTO;
import com.cg.loginservice.dto.ValidationDTO;
import com.cg.loginservice.dto.LoginCredentialsDTO;
import com.cg.loginservice.dto.RegistrationDTO;
import com.cg.loginservice.service.LoginService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor

//// http://localhost:8090/

public class LoginController {

	@Autowired
	private LoginService loginService;

	// Credential is the Google Auth Credential Repository - Check UserTOTP.java for the normal class,
	// while UserTOTPDTO.java is the database entity.
	@Autowired
	private CredentialRepository credential;

	@Autowired
	private GoogleAuthenticator gAuth;

	@GetMapping(value = "/")
	public String home(){
		return "This is the Home Page for the Login Manager.";
	}

	// This method returns Validation having a boolean variable "valid". MAIN METHOD FOR LOGIN.
	// After this returns true, check 2FA status with "googleAuthStatus" Line 66, then
	// If 2FA status active, you need to ask for 2FA code and check for validity using
	// "validateKey" Line 123. If that return true. you can Login.
	// Alternatively, if login method return TRUE and 2FA is inactive, you can Login.
	@PostMapping(value = "/login")
	public ValidationDTO login(@RequestBody LoginCredentialsDTO credentials) {
		return new ValidationDTO(loginService.authenticateUser(credentials.getUserName(), credentials.getPassword(), 
		credentials.getRole()).getAuthConsent());
	}

	// This method is used to register a new candidate, This method check for unique combination of userName and role
	// If unique adds the credentials in login table ELSE throws exception
	// On successfull Addition, use "generate" method on Line 116, If requested for 2FA, to generate QR Code.
	// QR Code needs to be scanned by 'Google Authenticator App on PlayStore'.
	@PostMapping(value = "/register")
	public RegistrationDataDTO registerUser(@RequestBody RegistrationDTO newUser) {
		loginService.addCredentials(new LoginCredentialsDTO(newUser.getUserName(), newUser.getPassword(), newUser.getRole(),
			newUser.getAuthConsent()));
		return new RegistrationDataDTO(newUser.getName(), newUser.getUserName(), newUser.getEmail(), newUser.getPhoneNo());
	}

	// 2FA = Two Factor Authentication with Google Auth
	// This return the status of 2FA
	@PostMapping(value = "/2FAStatus")
	public ValidationDTO googleAuthStatus(@RequestParam String userName, @RequestParam String role){
		return new ValidationDTO(loginService.getUser(userName, role).getAuthConsent());
	}

	// This method changes 2FA status in case, user requests to disable or enable the 2FA.
	// It just flips the value, If it was enabled, this will return false, else true.
	@PostMapping(value = "/change2FAStatus")
	public ValidationDTO changeAuthStatus(@RequestParam String userName, @RequestParam String role){
		boolean temp = loginService.changeAuthStatus(userName, role).getAuthConsent();
		if(!temp) credential.removeUser(userName+","+role);
		return new ValidationDTO(temp);
	}

	// This method is used to change the password, Before this you need to verify the user for the account,
	// he is going to change the password for.
	// For verification, you can give him options, if 2FA is enabled, ask for code, verify using "validateKey" Line 123
	// OR get the security question from your user table, verify that. OR BOTH. Your Choice.
	@PostMapping(value = "/changePassword")
	public ValidationDTO changePassword(@RequestParam String userName, @RequestParam String role, @RequestParam String newPassword){
		return new ValidationDTO(loginService.changePassword(userName, role, newPassword));
	}

	// This method deletes the user details from the login database and google authentication database.
	// It DOES NOT DISABLE, but REMOVES the DATA.
	@DeleteMapping("/removeCredentials")
	public String deleteCredentials(@RequestParam String userName, @RequestParam String role) {
		String result="";
		if(loginService.deleteCredentials(userName, role))
			result+="Login Details Deleted!\n";
		else result+="Login Details Not Found!\n";
		if(credential.removeUser(userName+","+role))
			result+="User 2FA Details Deleted!";
		else result+="User 2FA Details Not Found!";
		return result;
	}
	
	// This method is used to generate the QR Code for the user first time when, enabling 2FA.
	// Connot Generate Google Auth QR more than once for the same user.
	@SneakyThrows
	@GetMapping("/generate/{userName}/{role}")
	public void generate(@PathVariable String userName, @PathVariable String role, HttpServletResponse response)
			throws WriterException, IOException {
        final GoogleAuthenticatorKey key = gAuth.createCredentials(userName+","+role);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("login-service", userName+","+role, key);
		
		BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);

		ServletOutputStream outputStream = response.getOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
		outputStream.close();
    }

	// This method validates the Google Authentication 2FA Code with "userName,role" and "Code".
    @PostMapping("/validate/key")
    public ValidationDTO validateKey(@RequestBody ValidateCodeDTO authObj) {
        return new ValidationDTO(gAuth.authorizeUser(authObj.getUserNameCommaRole(), authObj.getCode()));
    }
}
