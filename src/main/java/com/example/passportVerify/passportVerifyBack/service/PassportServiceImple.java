package com.example.passportVerify.passportVerifyBack.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import com.example.passportVerify.passportVerifyBack.response.VerificationResponse;
import jakarta.validation.Validation;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
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
	ITesseract tesseract;

	@Autowired
	ValidationService validationService;

	public VerificationResponse registerPassport(PassportDataRequest passportDataRequest)
			throws TesseractException, IOException {
		try {

			PassportData passportData0 = passportDataRepository.findByNo(passportDataRequest.getPassportNumber());
			InputStream inputStream = passportDataRequest.getPassportDoc().getInputStream();
			BufferedImage bufferedImage = ImageIO.read(inputStream);

			String result = tesseract.doOCR(bufferedImage).replaceAll("\\s", "");
			log.info(result);
//		File image = new File(String.valueOf(passportDataRequest.getPassportDoc()));
//		String result = tesseract.doOCR(image);
			if (passportData0 == null) {
				if (result.contains(passportDataRequest.getFirstName()) && result.contains(passportDataRequest.getLastName())
						&& result.contains(passportDataRequest.getPassportNumber())) {
					PassportData passportData = new PassportData();
					passportData.setFirstName(passportDataRequest.getFirstName());
					passportData.setLastName(passportDataRequest.getLastName());
					passportData.setPassportNumber(passportDataRequest.getPassportNumber());
					passportData.setPhoneNumber(passportDataRequest.getPhoneNumber());
					passportData.setAge(passportDataRequest.getAge());
					passportData.setEmail(passportDataRequest.getEmail());

					passportDataRepository.save(passportData);
					VerificationResponse verificationResponse=new VerificationResponse("Verified and Registered Successfully");
					return verificationResponse;
				} else {
					log.debug("PassportService.registerPassport.end Issue in register");
					VerificationResponse verificationResponse1=new VerificationResponse("Details Not Matching Please check again!");
					return verificationResponse1;
				}

			} else {
				VerificationResponse verificationResponse2=new VerificationResponse("Registered Already");
				return verificationResponse2;
			}
		} catch (IOException e) {
			log.error("Error registering user",e);
			throw new RuntimeException("Error registering user", e);
		}catch (TesseractException e) {
			log.error("Error performing OCR", e);
			throw new TesseractException("Error performing OCR", e);
		}
	}
}
