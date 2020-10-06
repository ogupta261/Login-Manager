package com.cg.loginservice.controller;

import java.util.Collections;
import java.util.Map;

import com.cg.loginservice.authentication.CredentialRepository;
import com.cg.loginservice.dto.RegistrationDataDTO;
import com.cg.loginservice.dto.ValidateCodeDTO;
import com.cg.loginservice.dto.ValidationDTO;
import com.cg.loginservice.dto.LoginCredentialsDTO;
import com.cg.loginservice.dto.LoginDTO;
import com.cg.loginservice.dto.RegistrationDTO;
import com.cg.loginservice.service.LoginService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")

//// http://localhost:8090/

public class LoginController {

	@Autowired
	private LoginService loginService;

	// Credential is the Google Auth Credential Repository - Check UserTOTP.java for
	// the normal class,
	// while UserTOTPDTO.java is the database entity.
	@Autowired
	private CredentialRepository credential;

	@Autowired
	private GoogleAuthenticator gAuth;

	@GetMapping(value = "/")
	public String home() {
		return "This is the Home Page for the Login Manager.";
	}

	// This method returns LoginDTO. MAIN METHOD FOR LOGIN.
	// After this object has authConsent == true, you need to ask for 2FA code and
	// check for validity using
	// "validateKey" Line 123. If that return true. you can Login.
	// Alternatively, if authConsent == false means 2FA is inactive, you can Login.
	// If credentials are wrong Exception is thrown.
	@PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
	public LoginDTO login(@RequestBody LoginCredentialsDTO credentials) {
		return loginService.authenticateUser(credentials.getUserName(), credentials.getPassword(),
				credentials.getRole());
	}

	// This method is used to register a new candidate, This method check for unique
	// combination of userName and role
	// If unique adds the credentials in login table ELSE throws exception
	// On successfull Addition, use "generate" method on Line 116, If requested for
	// 2FA, to generate QR Code.
	// QR Code needs to be scanned by 'Google Authenticator App on PlayStore'.
	@PostMapping(value = "/register", produces = "application/json", consumes = "application/json")
	public RegistrationDataDTO registerUser(@RequestBody RegistrationDTO newUser) {
		loginService.addCredentials(new LoginCredentialsDTO(newUser.getUserName(), newUser.getPassword(),
				newUser.getRole(), newUser.getAuthConsent()));
		return new RegistrationDataDTO(newUser.getName(), newUser.getUserName(), newUser.getEmail(),
				newUser.getPhoneNo());
	}

	// 2FA = Two Factor Authentication with Google Auth
	// This return the status of 2FA
	@PostMapping(value = "/2FAStatus", produces = "application/json")
	public ValidationDTO googleAuthStatus(@RequestParam String userName, @RequestParam String role) {
		return new ValidationDTO(loginService.getUser(userName, role).getAuthConsent());
	}

	// This method changes 2FA status in case, user requests to disable or enable
	// the 2FA.
	// It just flips the value, If it was enabled, this will return false, else
	// true.\
	// i.e. the Changed 2FA status value.
	@PostMapping(value = "/change2FAStatus", produces = "application/json")
	public ValidationDTO changeAuthStatus(@RequestParam String userName, @RequestParam String role) {
		boolean temp = loginService.changeAuthStatus(userName, role).getAuthConsent();
		if (!temp)
			credential.removeUser(userName + "," + role);
		return new ValidationDTO(temp);
	}

	// This method is used to change the password, Before this you need to verify
	// the user for the account,
	// he is going to change the password for.
	// For verification, you can give him options, if 2FA is enabled, ask for code,
	// verify using "validateKey" Line 123
	// OR get the security question from your user table, verify that. OR BOTH. Your
	// Choice.
	@PostMapping(value = "/changePassword", produces = "application/json")
	public ValidationDTO changePassword(@RequestParam String userName, @RequestParam String role,
			@RequestParam String newPassword) {
		return new ValidationDTO(loginService.changePassword(userName, role, newPassword));
	}

	// This method deletes the user details from the login database and google
	// authentication database.
	// It DOES NOT DISABLE, but REMOVES the DATA.
	@DeleteMapping(value = "/removeCredentials", produces = "application/json")
	public String deleteCredentials(@RequestParam String userName, @RequestParam String role) {
		String result = "";
		if (loginService.deleteCredentials(userName, role))
			result += "Login Details Deleted! ";
		else
			result += "Login Details Not Found! ";
		if (credential.removeUser(userName + "," + role))
			result += "User 2FA Details Deleted!";
		else
			result += "User 2FA Details Not Found!";
		return result;
	}

	// This method is used to generate the QR Code for the user first time when,
	// enabling 2FA.
	// Connot Generate Google Auth QR more than once for the same user.
	@GetMapping(value = "/generate/{userName}/{role}", produces = "application/json")
	public Map<String, String> generate(@PathVariable String userName, @PathVariable String role) {

		final GoogleAuthenticatorKey key = gAuth.createCredentials(userName+","+role);
		String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("login-service", userName+","+role, key);
		String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
		return Collections.singletonMap("response", QR_PREFIX+otpAuthURL) ;
		//In case u need backend to generate QR. Uncomment belo lines and change method return to void and comment above 2 lines.
        // QRCodeWriter qrCodeWriter = new QRCodeWriter();
		// BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);

		// ServletOutputStream outputStream = response.getOutputStream();
		// MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
		// outputStream.close();
    }

	// This method validates the Google Authentication 2FA Code with "userName,role" and "Code".
    @PostMapping(value = "/validate/key", produces = "application/json", consumes = "application/json")
    public ValidationDTO validateKey(@RequestBody ValidateCodeDTO authObj) {
        return new ValidationDTO(gAuth.authorizeUser(authObj.getUserNameCommaRole(), authObj.getCode()));
    }
}
