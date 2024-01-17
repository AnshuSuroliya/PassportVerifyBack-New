package com.example.passportVerify.passportVerifyBack.service;

import com.example.passportVerify.passportVerifyBack.entity.User;
import com.example.passportVerify.passportVerifyBack.exception.UserException;
import com.example.passportVerify.passportVerifyBack.exception.ValidationException;
import com.example.passportVerify.passportVerifyBack.repository.UserRepository;
import com.example.passportVerify.passportVerifyBack.request.GetRequest;
import com.example.passportVerify.passportVerifyBack.request.Login;
import com.example.passportVerify.passportVerifyBack.response.LoginResponse;
import com.example.passportVerify.passportVerifyBack.response.SignupResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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
@Override
    public SignupResponse signUp(User user) throws UserException, ValidationException {
        if(validationService.sigupValidation(user)) {
            try {
                User user1 = userRepository.findByEmail(user.getEmail());
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                if (user1 == null) {
                    User u = new User();
                    u.setFirstName(user.getFirstName());
                    u.setLastName(user.getLastName());
                    u.setEmail(user.getEmail());
                    u.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                    u.setPhoneNumber(user.getPhoneNumber());
                    userRepository.save(u);
                    SignupResponse signupResponse = new SignupResponse("User Registered Successfully");
                    log.info("User Registered successfully");
                    return signupResponse;
                } else {
                    SignupResponse signupResponse1 = new SignupResponse("User Already Present");
                    log.info("User Already Present");
                    return signupResponse1;
                }
            }catch (Exception e){
                log.error("Error in creating account");
                throw new UserException("Error in creating account");
            }
        } else {
            log.info("Provided input syntax in incorrect");
            throw new ValidationException("Provided input syntax in incorrect");
        }
    }
    @Override
    public LoginResponse signIn(Login login) throws UserException,ValidationException{
            if(validationService.signinValidation(login)) {

                    User user = userRepository.findByEmail(login.getEmail());
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
                        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
                        if (authentication.isAuthenticated()) {
                            String jwt = (jwtService.generateToken(login.getEmail()));

                            LoginResponse loginResponse = new LoginResponse(true, jwt, "Login Successfull!", login.getEmail());
                            log.info("Login Successfull");
                            return loginResponse;
                        }
                        else {
                            LoginResponse loginResponse = new LoginResponse(false, null, "Wrong Password", null);
                            log.info("Wrong Password");
                            return loginResponse;
                        }
                    }catch (Exception e){
                        log.error("error in login");
                        throw new UserException("error in login");
                    }
            }
            else {
                log.info("Provided input syntax in wrong");
                throw new ValidationException("Provided input syntax in wrong");
            }
    }

    @Override
    public User getUser(GetRequest getRequest) throws UserException {
        User user=userRepository.findByEmail(getRequest.getEmail());
        if(user==null){
            log.error("Error fetching user");
            throw new UserException("Error fetching user");
        }
        return user;
    }
}
