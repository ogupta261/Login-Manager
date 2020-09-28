package com.cg.loginservice.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cg.loginservice.dto.LoginCredentialsDTO;
import com.cg.loginservice.id.LoginCredentialsId;

@Entity
@Table(name="login_credentials")
@IdClass(LoginCredentialsId.class)
public class LoginCredentials {
    @Id
    @Column(name = "user_name")
    private String userName;

    @Id
    @Column(name = "role")
    private String role;

    @Column(name = "password")
    private String password;

    // number(1) is used in database for Boolean
    @Column(name = "auth_consent")
    private boolean authConsent;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "last_login_date")
    private Date lastLoginDate;

    public LoginCredentials() {
        super();
    }

    public LoginCredentials(String userName, String password, String role, boolean authConsent) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.authConsent = authConsent;
    }

    public LoginCredentials(LoginCredentialsDTO credentials) {
        userName = credentials.getUserName();
        password = credentials.getPassword();
        role = credentials.getRole();
        authConsent = credentials.getAuthConsent();
        lastLoginDate = credentials.getLastLoginDate();
	}

	public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean getAuthConsent() {
        return authConsent;
    }

    public void setAuthConsent(boolean authConsent) {
        this.authConsent = authConsent;
    }

    @Override
    public String toString() {
        return "LoginCredentials [password=" + password + ", role=" + role + ", userName=" + userName + ", 2FA="
                + authConsent + ", lastLoginDate=" + lastLoginDate.toString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (this.getClass() != obj.getClass())
            return false;
        LoginCredentials temp = (LoginCredentials) obj;
        return this.userName.equals(temp.getUserName()) && this.password.equals(temp.getPassword()) && this.role.equals(temp.getRole()) && this.authConsent==temp.getAuthConsent();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}