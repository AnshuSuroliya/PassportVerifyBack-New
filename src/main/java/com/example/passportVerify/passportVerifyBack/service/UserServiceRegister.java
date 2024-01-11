package com.example.passportVerify.passportVerifyBack.service;

import com.example.passportVerify.passportVerifyBack.entity.User;
import com.example.passportVerify.passportVerifyBack.exception.UserException;
import com.example.passportVerify.passportVerifyBack.request.Login;
import com.example.passportVerify.passportVerifyBack.response.LoginResponse;

public interface UserServiceRegister {
    public String signUp(User user) throws UserException;
    public LoginResponse signIn(Login login) throws UserException;
}
