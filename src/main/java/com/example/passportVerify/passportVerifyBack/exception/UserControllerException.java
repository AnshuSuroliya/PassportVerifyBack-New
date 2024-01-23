package com.example.passportVerify.passportVerifyBack.exception;

import com.example.passportVerify.passportVerifyBack.entity.LoginAttempt;
import com.example.passportVerify.passportVerifyBack.response.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class UserControllerException {


        @ExceptionHandler(value = UserException.class)
        public ResponseEntity<LoginResponse> handleInvalidCredentialsException(){
            LoginResponse loginResponse=new LoginResponse();
            loginResponse.setEmail(null);
            loginResponse.setJwt(null);
            loginResponse.setMessage("Wrong Password");
            loginResponse.setSuccess(false);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }

    }

