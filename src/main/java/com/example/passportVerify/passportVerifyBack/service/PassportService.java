package com.example.passportVerify.passportVerifyBack.service;

import com.example.passportVerify.passportVerifyBack.request.PassportDataRequest;
import com.example.passportVerify.passportVerifyBack.response.VerificationResponse;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PassportService {
    public VerificationResponse registerPassport(PassportDataRequest passportDataRequest)
            throws TesseractException, IOException;
}
