package com.cg.loginservice.id;

import java.io.Serializable;


public class LoginCredentialsId implements Serializable{

    private static final long serialVersionUID = 1L;
    private String userName;
    private String role;

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userId) {
        this.userName = userId;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public LoginCredentialsId(){
        super();
    }
    public LoginCredentialsId(String userId, String role){
        this.userName = userId;
        this.role = role;
    }
}
