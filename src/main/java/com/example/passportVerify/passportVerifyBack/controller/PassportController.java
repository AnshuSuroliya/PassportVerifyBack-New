package com.example.passportVerify.passportVerifyBack.controller;

import com.example.passportVerify.passportVerifyBack.request.PassportDataRequest;
import com.example.passportVerify.passportVerifyBack.response.VerificationResponse;
import com.example.passportVerify.passportVerifyBack.service.PassportServiceImple;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/api")
public class PassportController {
	@Autowired
	PassportServiceImple passportServiceImple;

	@PostMapping(path="/verify", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<VerificationResponse> verify(@RequestParam("passportDoc") MultipartFile passportDoc,
													   @ModelAttribute PassportDataRequest passportDataRequest
			) throws TesseractException, IOException {
		return new ResponseEntity<>(passportServiceImple.registerPassport(passportDataRequest,passportDoc),
				HttpStatus.ACCEPTED);
	}
}
