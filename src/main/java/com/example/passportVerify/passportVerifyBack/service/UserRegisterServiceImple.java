package com.example.passportVerify.passportVerifyBack.service;

import com.example.passportVerify.passportVerifyBack.entity.LoginAttempt;
import com.example.passportVerify.passportVerifyBack.entity.User;
import com.example.passportVerify.passportVerifyBack.exception.UserException;
import com.example.passportVerify.passportVerifyBack.exception.ValidationException;
import com.example.passportVerify.passportVerifyBack.repository.LoginAttemptRepository;
import com.example.passportVerify.passportVerifyBack.repository.UserRepository;
import com.example.passportVerify.passportVerifyBack.request.GetRequest;
import com.example.passportVerify.passportVerifyBack.request.Login;
import com.example.passportVerify.passportVerifyBack.response.LoginResponse;
import com.example.passportVerify.passportVerifyBack.response.SignupResponse;
import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
public class UserRegisterServiceImple implements UserServiceRegister{
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;

    @Autowired
    ValidationService validationService;

    @Autowired
    LoginAttemptRepository loginAttemptRepository;

    int count=0;
@Override
    public SignupResponse signUp(User user) throws UserException, ValidationException {
        if(validationService.nameValidation(user.getFirstName()) && validationService.nameValidation(user.getLastName()) && validationService.emailValidation(user.getEmail()) && validationService.phoneNumberValidation(user.getPhoneNumber())) {
            try {
                User user1 = userRepository.findByEmail(user.getEmail().toLowerCase());
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                if (user1 == null) {
                    User u = new User();
                    u.setFirstName(user.getFirstName());
                    u.setLastName(user.getLastName());
                    u.setEmail(user.getEmail().toLowerCase());
                    u.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                    u.setPhoneNumber(user.getPhoneNumber());
                    userRepository.save(u);
                    SignupResponse signupResponse = new SignupResponse("User Registered Successfully",true);
                    log.info("User Registered successfully");
                    return signupResponse;
                } else {
                    SignupResponse signupResponse1 = new SignupResponse("User Already Present",false);
                    log.info("User Already Present");
                    return signupResponse1;
                }
            }catch (Exception e){
                log.error("Error in creating account");
                SignupResponse signupResponse=new SignupResponse("Some error occured in creating account",false);
                return new ResponseEntity<SignupResponse>(signupResponse, HttpStatus.OK).getBody();
            }
        } else {
            log.info("Provided input syntax in incorrect");
            SignupResponse signupResponse=new SignupResponse("Provided input syntax is incorrect",false);
            return signupResponse;
        }
    }
    @Override
    public LoginResponse signIn(Login login) throws UserException,ValidationException{
            if(validationService.emailValidation(login.getEmail())) {

                    User user = userRepository.findByEmail(login.getEmail().toLowerCase());
                    if(user.getLocked()){

                        log.info("Your account is Locked due to too many failed attempts.Try after 5 min.");
                        LoginResponse loginResponse=new LoginResponse(false,null,"Your account is Locked due to too many failed attempts.Try after 5 min.",null);
                        return loginResponse;
                    }
                    if (user == null) {
                        LoginResponse loginResponse = new LoginResponse(false, null, "Wrong Email",null);
                        log.info("Wrong Email");
                        return loginResponse;
                    }
//            if(!login.getEmail().equals(user.getEmail())){
//                LoginResponse loginResponse=new LoginResponse(false,null,"Wrong Email");
//                return loginResponse;
//            }

                    try {
                        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail().toLowerCase(), login.getPassword()));
                        if (authentication.isAuthenticated()) {
                            String jwt = (jwtService.generateToken(login.getEmail().toLowerCase()));
                            LoginAttempt loginAttempt=new LoginAttempt();
                            loginAttempt.setFailedAttempt(0);
                            LoginResponse loginResponse = new LoginResponse(true, jwt, "Login Successfull!", login.getEmail().toLowerCase());
                            log.info("Login Successfull");
                            return loginResponse;
                        }
                        else {
                            LoginResponse loginResponse = new LoginResponse(false, null, "Wrong Password", null);
                            log.info("Wrong Password");
                            return loginResponse;
                        }
                    }catch (Exception e){
                        log.error("Wrong Password");
                        LoginAttempt loginAttempt=loginAttemptRepository.findByUserId(user);
                        if(loginAttempt==null){
                            LoginAttempt loginAttempt1=new LoginAttempt();
                            loginAttempt1.setFailedAttempt(count+1);
                            loginAttempt1.setTime(new Date());
                            loginAttempt1.setUserId(user);
                            loginAttemptRepository.save(loginAttempt1);
                            LoginResponse loginResponse=new LoginResponse(false,null,"Wrong Password",null);
                            return loginResponse;
                        }

                        if(loginAttempt.getFailedAttempt()==2){
                            LoginAttempt loginAttempt1=new LoginAttempt();
                            loginAttempt1.setFailedAttempt(0);
                            loginAttempt1.setTime(new Date());
                            loginAttempt1.setUserId(user);
                            loginAttemptRepository.save(loginAttempt1);
                            user.setFailedTime(new Date());
                            user.setLocked(true);
                            LoginResponse loginResponse=new LoginResponse(false,null,"Due to too many wrong attempts your account is locked for 10 min.",null);
                            return loginResponse;
                        }
                        LoginAttempt loginAttempt1=new LoginAttempt();
                        loginAttempt1.setFailedAttempt(count+1);
                        loginAttempt1.setTime(new Date());
                        loginAttempt1.setUserId(user);
                        loginAttemptRepository.save(loginAttempt);
                        throw new UserException("Wrong Password");
//                        LoginResponse loginResponse=new LoginResponse(false,null,"error in login",null);
//                        return loginResponse;
                    }
            }
            else {
                log.info("Provided input syntax is wrong");
                LoginResponse loginResponse=new LoginResponse(false,null,"Provided input syntax is incorrect",null);
                return loginResponse;
            }
    }

    @Override
    public User getUser(GetRequest getRequest) throws UserException {
        User user=userRepository.findByEmail(getRequest.getEmail().toLowerCase());
        if(user==null){
            log.error("Error fetching user");
            throw new UserException("Error fetching user");
        }
        return user;
    }
}
