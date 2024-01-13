package com.example.passportVerify.passportVerifyBack.controller;

import com.example.passportVerify.passportVerifyBack.exception.UserException;
import com.example.passportVerify.passportVerifyBack.exception.ValidationException;
import com.example.passportVerify.passportVerifyBack.response.SignupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.passportVerify.passportVerifyBack.entity.User;
import com.example.passportVerify.passportVerifyBack.repository.UserRepository;
import com.example.passportVerify.passportVerifyBack.request.Login;
import com.example.passportVerify.passportVerifyBack.response.LoginResponse;
import com.example.passportVerify.passportVerifyBack.service.UserRegisterServiceImple;

@RestController
@RequestMapping("/v1/auth")
public class UserController {
	@Autowired
	UserRepository userRepository;
	@Autowired
    UserRegisterServiceImple userRegisterServiceImple;

	@PostMapping("/signup")
	public ResponseEntity<SignupResponse> register(@RequestBody User user) throws UserException, ValidationException {
		return new ResponseEntity<>(userRegisterServiceImple.signUp(user), HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public ResponseEntity<LoginResponse> login(@RequestBody Login login) throws UserException,ValidationException {
		return new ResponseEntity<>(userRegisterServiceImple.signIn(login), HttpStatus.OK);
	}

}
