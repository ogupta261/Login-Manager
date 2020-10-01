package com.cg.loginservice.dto;

public class LoginCredentialsDTO {
    private String userName;
    private String role;
    private String password;
    private boolean authConsent;

    public LoginCredentialsDTO() {
        super();
    }
    public LoginCredentialsDTO(String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }
    public LoginCredentialsDTO(String userName, String password, String role, boolean authConsent) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.authConsent = authConsent;
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
        return "LoginCredentials [password=" + password + ", role=" + role + ", userName=" + userName + ", 2FA=" + authConsent + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (this.getClass() != obj.getClass())
            return false;
        LoginCredentialsDTO temp = (LoginCredentialsDTO)obj;
        return this.userName.equals(temp.getUserName()) && this.password.equals(temp.getPassword()) && this.role.equals(temp.getRole()) && this.authConsent==temp.getAuthConsent();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
