package com.cg.loginservice.dto;

public class ValidationDTO {
    private boolean valid;
    public boolean getValid(){
        return valid;
    }
    public void setValid(boolean valid){
        this.valid = valid;
    }
    public ValidationDTO(){
        super();
    }
    public ValidationDTO(boolean valid){
        this.valid = valid;
    }
}
