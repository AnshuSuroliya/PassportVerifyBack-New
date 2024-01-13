package com.example.passportVerify.passportVerifyBack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
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
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	@OneToOne
	private Address address;
	@NotNull
	private String age;
	@NotNull
	private String phoneNumber;
	@NotNull
	private String email;
	@NotNull
	private String passportNumber;
	@NotNull
	private Byte passportDoc;


}
