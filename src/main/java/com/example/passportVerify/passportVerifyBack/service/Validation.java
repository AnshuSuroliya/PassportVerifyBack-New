package com.example.passportVerify.passportVerifyBack.service;

import com.example.passportVerify.passportVerifyBack.entity.User;
import com.example.passportVerify.passportVerifyBack.exception.ValidationException;
import com.example.passportVerify.passportVerifyBack.request.Login;
import com.example.passportVerify.passportVerifyBack.request.PassportDataRequest;
import com.example.passportVerify.passportVerifyBack.response.SignupResponse;

public interface Validation {
    public Boolean sigupValidation(User user) throws ValidationException;
    public Boolean signinValidation(Login login)throws ValidationException;
    public Boolean verifyValidation(PassportDataRequest passportDataRequest)throws ValidationException;
}
