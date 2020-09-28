package com.cg.loginservice.dto;

import java.util.Date;

import com.cg.loginservice.entity.LoginCredentials;

public class LoginDTO {
    private String userName;
    private String role;
    private boolean authConsent;
    private Date lastLoginDate;

    public LoginDTO() {
        super();
    }
    public LoginDTO(String userName,String role, boolean authConsent, Date lastLoginDate) {
        this.userName = userName;
        this.role = role;
        this.authConsent = authConsent;
        this.lastLoginDate = lastLoginDate;
    }
    public LoginDTO(LoginCredentials login){
        this.userName = login.getUserName();
        this.role = login.getRole();
        this.authConsent = login.getAuthConsent();
        this.lastLoginDate = login.getLastLoginDate();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Override
    public String toString() {
        return "LoginDTO [role=" + role + ", userName=" + userName + ", 2FA=" + authConsent + ", lastLoginDate=" + lastLoginDate.toString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (this.getClass() != obj.getClass())
            return false;
        LoginDTO temp = (LoginDTO)obj;
        return this.userName.equals(temp.getUserName()) && this.role.equals(temp.getRole()) && this.authConsent==temp.getAuthConsent();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
