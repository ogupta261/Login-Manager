package com.cg.loginservice;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.cg.loginservice.dao.LoginDAO;
import com.cg.loginservice.dao.UserOTPDAO;
import com.cg.loginservice.entity.LoginCredentials;
import com.cg.loginservice.dto.LoginCredentialsDTO;
import com.cg.loginservice.dto.LoginDTO;
import com.cg.loginservice.exceptions.CustomException;
import com.cg.loginservice.service.LoginService;
import com.cg.loginservice.service.LoginServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
class LoginServiceApplicationTests {
	
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	LoginDAO loginDAO;

	@Autowired
	UserOTPDAO userTOTPDAO;

	@Autowired
	LoginService loginService;
	
	@TestConfiguration
	static class EmployeeServiceImplTestContextConfiguration {

		@Bean
		public LoginService loginService() {
			return new LoginServiceImpl();
		}
	}

	@Test
	void whenAuthenticateByUsernameAndPasswordAndRole_thenReturnLoginDTO() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		temp.setLastLoginDate(Date.from(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		entityManager.persist(temp);
		entityManager.flush();
		LoginDTO found = loginService.authenticateUser("user", "Pass123", "admin");
		assertEquals(new LoginDTO(temp), found);
	}

	@Test
	void whenAuthenticateByWrongUsernameAndPasswordAndRole_thenThrowException() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		entityManager.persist(temp);
		entityManager.flush();
		assertEquals("Credentials Invalid!", assertThrows(
			CustomException.class,()
			 -> loginService.authenticateUser("user1", "Pass123", "admin")).getMessage());
	}
	
	@Test
	void whenAuthenticateByBlankUsernameAndPasswordAndRole_thenThrowException() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		entityManager.persist(temp);
		entityManager.flush();
		assertEquals("Username cannot be Empty!", assertThrows(
			CustomException.class,()
			 -> loginService.authenticateUser("", "Pass123", "admin")).getMessage());
	}

	@Test
	void whenAuthenticateByUsernameAndEmptyPasswordAndRole_thenThrowException() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		entityManager.persist(temp);
		entityManager.flush();
		assertEquals("Password cannot be Empty!", assertThrows(
			CustomException.class,()
			 -> loginService.authenticateUser("user1", "", "admin")).getMessage());
	}

	@Test
	void whenAuthenticateByUsernameAndIncorrectPasswordAndRole_thenThrowException() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		entityManager.persist(temp);
		entityManager.flush();
		assertEquals("Password not following Policy. Greater than 6 characters, Starting with Capital Letter.", 
				assertThrows(
					CustomException.class,()
					-> loginService.authenticateUser("user1", "Pass", "admin")).getMessage());
	}

	@Test
	void whenAuthenticateByUsernameAndPasswordAndEmptyRole_thenThrowException() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		entityManager.persist(temp);
		entityManager.flush();
		assertEquals("Role cannot be Empty!", assertThrows(
			CustomException.class,()
			 -> loginService.authenticateUser("user1", "Pass123", "")).getMessage());
	}

	@Test
	void whenGetUserByUsernameAndRole_thenReturnLoginCredentials() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		entityManager.persist(temp);
		entityManager.flush();
		LoginDTO found = loginService.getUser("user", "admin");
		assertEquals(new LoginDTO(temp), found);
	}

	@Test
	void whenGetByWrongUsernameAndRole_thenThrowException() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		entityManager.persist(temp);
		entityManager.flush();

		assertEquals("User not Found!",
			assertThrows(
				CustomException.class, ()
				 -> loginService.getUser("user1", "admin")).getMessage());
	}

	@Test
	void whenChangeAuthStatusByUsernameAndRole_thenReturnChangedLoginCredentials() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		entityManager.persist(temp);
		entityManager.flush();

		assertEquals(false,loginService.changeAuthStatus("user", "admin").getAuthConsent());
	}

	@Test
	void whenChangeAuthStatusByWrongUsernameAndRole_thenThrowException() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		entityManager.persist(temp);
		entityManager.flush();

		assertEquals("User not Found!",
			assertThrows(
				CustomException.class, ()
				 -> loginService.changeAuthStatus("user1", "admin")).getMessage());
	}

	@Test
	void whenChangePasswordByUsernameAndRoleAndNewPassword_thenReturnTrue() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		entityManager.persist(temp);
		entityManager.flush();

		assertEquals(true, loginService.changePassword("user", "admin", "Newpass"));
	}

	@Test
	void whenChangePasswordByWrongUsernameAndRoleAndNewPassword_thenThrowException() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		entityManager.persist(temp);
		entityManager.flush();

		assertEquals("User not Found!",
			assertThrows(
				CustomException.class, ()
				 -> loginService.changePassword("user1", "admin", "Newpass")).getMessage());
	}

	@Test
	void whenAddCredentialsByLoginCredentialsDTO_thenReturnLoginDTO() {
		LoginDTO newUser = loginService.addCredentials(new LoginCredentialsDTO("user", "Password", "admin", true));
		assertEquals(true, newUser.getUserName().equals("user") && newUser.getRole().equals("admin") && newUser.getAuthConsent() && 
		newUser.getLastLoginDate().compareTo(Date.from(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()))==0);
	}

	@Test
	void whenAddCredentialsByRepeatedLoginCredentialsDTO_thenThrowException() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		entityManager.persist(temp);
		entityManager.flush();
		
		LoginCredentialsDTO tempDTO = new LoginCredentialsDTO("user", "Password", "admin", true);
		assertEquals("User Already Exists!",
			assertThrows(
				CustomException.class, ()
				 -> loginService.addCredentials(tempDTO)).getMessage());
	}

	@Test
	void whenDeleteCredentialsByUserNameAndRole_thenReturnTrue() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		entityManager.persist(temp);
		entityManager.flush();

		assertEquals(true, loginService.deleteCredentials("user", "admin"));
	}

	@Test
	void whenDeleteCredentialsByUserNameAndRoleNotInDatabase_thenReturnFalse() {
		LoginCredentials temp = new LoginCredentials("user", "Pass123", "admin", true);
		entityManager.persist(temp);
		entityManager.flush();

		assertEquals(false, loginService.deleteCredentials("user1", "admin"));
	}
}
