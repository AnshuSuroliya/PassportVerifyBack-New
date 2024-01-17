package com.example.passportVerify.passportVerifyBack.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@NoArgsConstructor

public class PassportDataRequest {
    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String city;
    private String zipcode;
    private String age;
    private String phoneNumber;
    private String email;
    private String passportNumber;
    private MultipartFile passportDoc;
}
