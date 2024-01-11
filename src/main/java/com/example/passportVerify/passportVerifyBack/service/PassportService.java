package com.example.passportVerify.passportVerifyBack.service;

import com.example.passportVerify.passportVerifyBack.request.PassportDataRequest;
import net.sourceforge.tess4j.TesseractException;

import java.io.IOException;

public interface PassportService {
    public String registerPassport(PassportDataRequest passportDataRequest)
            throws TesseractException, IOException;
}
