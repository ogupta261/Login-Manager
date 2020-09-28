package com.cg.loginservice.service;

import com.cg.loginservice.dto.LoginCredentialsDTO;
import com.cg.loginservice.dto.LoginDTO;

public interface LoginService {
	LoginDTO authenticateUser(String userName, String password, String role);
	LoginDTO addCredentials(LoginCredentialsDTO loginCredentialsDTO);
	boolean deleteCredentials(String userName, String role);
	LoginDTO getUser(String userName, String role);
	boolean changePassword(String userName, String role, String newPassword);
	LoginDTO changeAuthStatus(String userName, String role);
}
