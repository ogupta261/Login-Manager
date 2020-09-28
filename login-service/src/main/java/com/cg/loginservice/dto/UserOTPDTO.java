package com.cg.loginservice.dto;

import java.util.List;

public class UserOTPDTO {
    private String userNameCommaRole;
    private String secretKey;
    private int validationCode;
    private List<Integer> scratchCodes;

    public String getUserNameCommaRole() {
        return userNameCommaRole;
    }
    public void setUserNameCommaRole(String userNameCommaRole) {
        this.userNameCommaRole = userNameCommaRole;
    }
    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    public int getValidationCode() {
        return validationCode;
    }
    public void setValidationCode(int validationCode) {
        this.validationCode = validationCode;
    }
    public List<Integer> getScratchCodes() {
        return scratchCodes;
    }
    public void setScratchCodes(List<Integer> scratchCodes) {
        this.scratchCodes = scratchCodes;
    }
    
    public UserOTPDTO() {
        super();
    }

    public UserOTPDTO(String userNameCommaRole, String secretKey, int validationCode, List<Integer> scratchCodes){
        this.userNameCommaRole = userNameCommaRole;
        this.secretKey = secretKey;
        this.validationCode = validationCode;
        this.scratchCodes = scratchCodes;
    }

}
