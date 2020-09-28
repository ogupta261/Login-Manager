package com.cg.loginservice.dto;

public class ValidateCodeDTO {
    private Integer code;
    private String userNameCommaRole;
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getUserNameCommaRole() {
        return userNameCommaRole;
    }
    public void setUserNameCommaRole(String username) {
        this.userNameCommaRole = username;
    }
    public ValidateCodeDTO(){
        super();
    }
    public ValidateCodeDTO(Integer code, String username){
        this.code = code;
        this.userNameCommaRole = username;
    }
}