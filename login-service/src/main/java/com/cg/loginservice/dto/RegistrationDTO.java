package com.cg.loginservice.dto;

public class RegistrationDTO {
    private String name;
    private String userName;
    private String password;
    private String email;
    private String phoneNo;
    private String role;
    private boolean authConsent;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String id) {
        this.userName = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    public boolean getAuthConsent() {
        return authConsent;
    }
    public void setAuthConsent(boolean authConsent) {
        this.authConsent = authConsent;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public RegistrationDTO() {
        super();
    }
    public RegistrationDTO(String name, String id, String password, String email, String phoneNo, String role, boolean authConsent) {
        this.name = name;
        this.userName = id;
        this.password = password;
        this.email = email;
        this.phoneNo = phoneNo;
        this.role = role;
        this.authConsent = authConsent;
    }

}
