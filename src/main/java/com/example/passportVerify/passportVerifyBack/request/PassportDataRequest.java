package com.example.passportVerify.passportVerifyBack.request;

import com.example.passportVerify.passportVerifyBack.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PassportDataRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private Address address;
    private String age;
    private String phoneNumber;

    private String email;
    private String passportNumber;
    private MultipartFile passportDoc;


}
