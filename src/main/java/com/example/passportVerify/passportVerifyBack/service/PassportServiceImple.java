package com.example.passportVerify.passportVerifyBack.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.passportVerify.passportVerifyBack.entity.PassportData;
import com.example.passportVerify.passportVerifyBack.repository.PassportDataRepository;
import com.example.passportVerify.passportVerifyBack.request.PassportDataRequest;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;

@Service
@Slf4j
public class PassportServiceImple implements PassportService{
	@Autowired
	PassportDataRepository passportDataRepository;
	
	@Autowired
	ITesseract tesseract;

	public String registerPassport(PassportDataRequest passportDataRequest)
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
					passportData.setAddress(passportDataRequest.getAddress());
					passportData.setEmail(passportDataRequest.getEmail());
					passportDataRepository.save(passportData);
					return "Registered Successfully";
				} else {
					log.debug("PassportService.registerPassport.end Issue in register");
					return "Invalid Details";
				}

			} else return "Registered Already";
		} catch (IOException e) {
			log.error("Error registering user",e);
			throw new RuntimeException("Error registering user", e);
		}catch (TesseractException e) {
			log.error("Error performing OCR", e);
			throw new TesseractException("Error performing OCR", e);
		}
	}
}
