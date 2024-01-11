package com.example.passportVerify.passportVerifyBack.response;


public class LoginResponse {
    private Boolean success;
    private String jwt;
    private String message;


    public LoginResponse() {

    }

    public LoginResponse(Boolean success, String jwt, String message) {
        this.success = success;
        this.jwt = jwt;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
