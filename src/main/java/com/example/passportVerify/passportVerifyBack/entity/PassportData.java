package com.example.passportVerify.passportVerifyBack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PassportData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String firstName;
	private String lastName;

	@OneToOne
	private Address address;

	private String age;
	private String phoneNumber;
	private String email;
	private String passportNumber;
	private Byte passportDoc;


}
