package com.example.passportVerify.passportVerifyBack.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import com.example.passportVerify.passportVerifyBack.entity.Address;
import com.example.passportVerify.passportVerifyBack.exception.PassportException;
import com.example.passportVerify.passportVerifyBack.exception.ValidationException;
import com.example.passportVerify.passportVerifyBack.repository.AddressRepository;
import com.example.passportVerify.passportVerifyBack.request.GetRequest;
import com.example.passportVerify.passportVerifyBack.response.VerificationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.passportVerify.passportVerifyBack.entity.PassportData;
import com.example.passportVerify.passportVerifyBack.repository.PassportDataRepository;
import com.example.passportVerify.passportVerifyBack.request.PassportDataRequest;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

@Service
@Slf4j
public class PassportServiceImple implements PassportService{

	@Autowired
	PassportDataRepository passportDataRepository;

	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	ITesseract tesseract;

	@Autowired
	ValidationService validationService;

	@Override
	public VerificationResponse registerPassport(PassportDataRequest passportDataRequest)
			throws TesseractException, IOException , ValidationException {

			if(validationService.verifyValidation(passportDataRequest)) {
				try {
					PassportData passportData0 = passportDataRepository.findByNo(passportDataRequest.getPassportNumber());


					if (passportData0 == null) {
						InputStream inputStream = passportDataRequest.getPassportDoc().getInputStream();
						BufferedImage bufferedImage = ImageIO.read(inputStream);

						String result = tesseract.doOCR(bufferedImage).replace("[-+.^:,><'?_ !`~@#$%&*()]","").replace("/r","").replace("/n","").replace("\\s","").toUpperCase();
						log.info(result);

						if (!result.contains(passportDataRequest.getFirstName().toUpperCase())) {
							log.error("First Name Does Not Match");
							VerificationResponse verificationResponse=new VerificationResponse("First Name Does Not Match");
							return verificationResponse;
						}
						if(!result.contains(passportDataRequest.getLastName().toUpperCase())) {
							log.error("Last Name Does Not Match");
							VerificationResponse verificationResponse=new VerificationResponse("Last Name does not match");
							return verificationResponse;
						}
						if(!result.contains(passportDataRequest.getPassportNumber())) {
							log.error("Passport Number Does Not Match");
							VerificationResponse verificationResponse=new VerificationResponse("Passport Number does not match");
							return verificationResponse;
						}
							PassportData passportData = new PassportData();
							passportData.setFirstName(passportDataRequest.getFirstName());
							passportData.setLastName(passportDataRequest.getLastName());
							passportData.setPassportNumber(passportDataRequest.getPassportNumber());
							passportData.setPhoneNumber(passportDataRequest.getPhoneNumber());
							passportData.setAge(passportDataRequest.getAge());
							passportData.setEmail(passportDataRequest.getEmail());
							Address address=new Address();
							address.setAddressLine1(passportDataRequest.getAddressLine1());
							address.setAddressLine2(passportDataRequest.getAddressLine2());
							address.setCity(passportDataRequest.getCity());
							address.setState(passportDataRequest.getState());
							address.setZipcode(passportDataRequest.getZipcode());
							addressRepository.save(address);
							passportData.setAddress(address);
							passportDataRepository.save(passportData);
							VerificationResponse verificationResponse = new VerificationResponse("Verified and Registered Successfully");
							log.info("Verified and Registered Successfully");
							return verificationResponse;

					} else {
						VerificationResponse verificationResponse2 = new VerificationResponse("Registered Already");
						log.info("Registered Already");
						return verificationResponse2;
					}
				} catch (TesseractException e) {
					log.error("Error performing OCR", e);
					throw new TesseractException("Error performing OCR", e);
				}
			} else {
				log.info("Provided input syntax is incorrect");
				throw new ValidationException("Provided input is syntax incorrect");

			}

	}

	@Override
	public PassportData getPassport(GetRequest getRequest) throws PassportException {
		log.info(getRequest.getEmail());
		PassportData passportData=passportDataRepository.findByEmail(getRequest.getEmail());
		if(passportData==null){
			log.error("Error Fetching the details");
			throw new PassportException("Error Fetching the details");
		}
		else return passportData;
	}
}
