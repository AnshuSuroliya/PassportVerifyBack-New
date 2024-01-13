package com.example.passportVerify.passportVerifyBack.service;

import com.example.passportVerify.passportVerifyBack.entity.User;
import com.example.passportVerify.passportVerifyBack.exception.ValidationException;
import com.example.passportVerify.passportVerifyBack.request.Login;
import com.example.passportVerify.passportVerifyBack.request.PassportDataRequest;
import com.example.passportVerify.passportVerifyBack.response.SignupResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ValidationService implements Validation{
    @Value("${validation.signup.name.regEx}")
    private String name;

    @Value("${validation.signup.number.regEx1}")
    private String number;

    @Value("${validation.email.regEx2}")
    private String email;

    @Value("${validation.passportNumber.regEx3}")
    private String passportNumber;

    @Value("${validation.address.regEx4}")
    private String address;
    @Override
    public Boolean sigupValidation(User user) throws ValidationException {
        if(user.getFirstName().matches(name) && user.getLastName().matches(name) && user.getPhoneNumber().matches(number) && user.getEmail().matches(email)){
            return true;
        }
        else return false;
    }
    @Override
    public Boolean signinValidation(Login login)throws ValidationException{
        if(login.getEmail().matches(email)){
            return true;
        }
        else return false;
    }

    @Override
    public Boolean verifyValidation(PassportDataRequest passportDataRequest) throws ValidationException {
        if(passportDataRequest.getFirstName().matches(name) && passportDataRequest.getLastName().matches(name) && passportDataRequest.getPhoneNumber().matches(number) && passportDataRequest.getEmail().matches(email) && passportDataRequest.getAge().matches(number)
        && passportDataRequest.getZipcode().matches(number)  &&
        passportDataRequest.getCity().matches(name) && passportDataRequest.getState().matches(name) && passportDataRequest.getPassportNumber().matches(passportNumber)){
        return true;
        }
        else return false;
    }
}
