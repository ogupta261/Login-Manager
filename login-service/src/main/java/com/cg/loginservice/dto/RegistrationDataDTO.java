package com.cg.loginservice.dto;

public class RegistrationDataDTO {
    private String name;
    private String userName;
    private String email;
    private String phoneNo;

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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
    public void setPhoneNo(String phoneno) {
        this.phoneNo = phoneno;
    }
    public RegistrationDataDTO() {
        super();
    }
    public RegistrationDataDTO(String name, String id, String email, String phoneno) {
        this.name = name;
        this.userName = id;
        this.email = email;
        this.phoneNo = phoneno;
    }
}
