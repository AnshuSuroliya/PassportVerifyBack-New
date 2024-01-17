package com.example.passportVerify.passportVerifyBack.service;

import com.example.passportVerify.passportVerifyBack.entity.Address;
import com.example.passportVerify.passportVerifyBack.entity.PassportData;
import com.example.passportVerify.passportVerifyBack.exception.PassportException;
import com.example.passportVerify.passportVerifyBack.exception.ValidationException;
import com.example.passportVerify.passportVerifyBack.repository.AddressRepository;
import com.example.passportVerify.passportVerifyBack.repository.PassportDataRepository;
import com.example.passportVerify.passportVerifyBack.request.GetRequest;
import com.example.passportVerify.passportVerifyBack.request.PassportDataRequest;
import com.example.passportVerify.passportVerifyBack.response.VerificationResponse;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class PassportServiceImpleTest {


        @Mock
        private PassportDataRepository passportDataRepository;

        @Mock
        private AddressRepository addressRepository;

        @Mock
        private ValidationService validationService;

        @Mock
        private ITesseract tesseract;
        @InjectMocks
        private PassportServiceImple passportService;

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void testRegisterPassport_Success() throws TesseractException, IOException, ValidationException {

            PassportDataRequest request = new PassportDataRequest();
            request.setFirstName("Pankaj");
            request.setLastName("Sharma");
            request.setPhoneNumber("7890562300");
            request.setEmail("pankaj@gmail.com");
            request.setAddressLine1("dhsgd");
            request.setAddressLine2("sdgsdhd");
            request.setCity("jaipur");
            request.setState("rajasthan");
            request.setZipcode("302029");
            request.setAge("25");
            request.setPassportNumber("F27284823");
            Address address=new Address();
            address.setAddressLine1(request.getAddressLine1());
            address.setAddressLine2(request.getAddressLine2());
            address.setCity(request.getAddressLine2());
            address.setState(request.getState());
            address.setZipcode(request.getZipcode());
            PassportData passportData=new PassportData();
            passportData.setAddress(address);
            passportData.setPassportNumber(request.getPassportNumber());
            passportData.setEmail(request.getEmail());
            passportData.setFirstName(request.getFirstName());
            passportData.setLastName(request.getLastName());
            passportData.setAge(request.getAge());
            passportData.setPhoneNumber(request.getPhoneNumber());
            MockMultipartFile imageFile = new MockMultipartFile("aadhaarImageFile", "aadhaar4.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());
            request.setPassportDoc(imageFile);
            String extractedText = "F27284823PankajSharma";
            BufferedImage bufferedImage=null;
            when(passportDataRepository.save(any())).thenReturn(passportData);
            when(addressRepository.save(any())).thenReturn(address);
            Mockito.when(tesseract.doOCR(bufferedImage)).thenReturn(extractedText);

            when(validationService.verifyValidation(request)).thenReturn(true);


//		when(tesseract.doOCR((File) any())).thenReturn("MockOCRResult");


            when(passportDataRepository.findByNo(anyString())).thenReturn(null);





            VerificationResponse response = passportService.registerPassport(request);



            assertEquals("Verified and Registered Successfully", response.getMessage());
        }

        @Test
        public void testRegisterPassport_FirstNameNotMatching() throws TesseractException, IOException, ValidationException {

            PassportDataRequest request = new PassportDataRequest();
            request.setFirstName("Rahul");
            request.setLastName("Agrawal");
            request.setPhoneNumber("7890562300");
            request.setEmail("pankaj@gmail.com");
            request.setAddressLine1("dhsgd");
            request.setAddressLine2("sdgsdhd");
            request.setCity("jaipur");
            request.setState("rajasthan");
            request.setZipcode("302029");
            request.setAge("25");
            request.setPassportNumber("L74568900");
            MockMultipartFile imageFile = new MockMultipartFile("aadhaarImageFile", "aadhaar4.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());
            request.setPassportDoc(imageFile);
            String extractedText = "F27284823AnujAgrawal";
            BufferedImage bufferedImage=null;
//            when(passportDataRepository.save(any())).thenReturn(new PassportData());
//            when(addressRepository.save(any())).thenReturn(new Address());
            Mockito.when(tesseract.doOCR(bufferedImage)).thenReturn(extractedText);
            when(validationService.verifyValidation(request)).thenReturn(true);
            when(passportDataRepository.findByNo(anyString())).thenReturn(null);
            VerificationResponse response = passportService.registerPassport(request);
            assertEquals("First Name Does Not Match", response.getMessage());
        }
        @Test
        public void testRegisterPassport_LastNameNotMatching() throws TesseractException, IOException, ValidationException {

            PassportDataRequest request = new PassportDataRequest();
            request.setFirstName("Rahul");
            request.setLastName("Agrawal");
            request.setPhoneNumber("7890562300");
            request.setEmail("pankaj@gmail.com");
            request.setAddressLine1("dhsgd");
            request.setAddressLine2("sdgsdhd");
            request.setCity("jaipur");
            request.setState("rajasthan");
            request.setZipcode("302029");
            request.setAge("25");
            request.setPassportNumber("L74568900");
            MockMultipartFile imageFile = new MockMultipartFile("aadhaarImageFile", "aadhaar4.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());
            request.setPassportDoc(imageFile);
            String extractedText = "F27284823RahulSharma";
            BufferedImage bufferedImage=null;
//            Mockito.when(passportDataRepository.save(any())).thenReturn(new PassportData());
//            Mockito.when(addressRepository.save(any())).thenReturn(new Address());
            Mockito.when(tesseract.doOCR(bufferedImage)).thenReturn(extractedText);
            when(validationService.verifyValidation(request)).thenReturn(true);
            when(passportDataRepository.findByNo(anyString())).thenReturn(null);
            VerificationResponse response = passportService.registerPassport(request);
            assertEquals("Last Name does not match", response.getMessage());
        }
        @Test
        public void testRegisterPassport_PassportNumberNotMatching() throws TesseractException, IOException, ValidationException {

            PassportDataRequest request = new PassportDataRequest();
            request.setFirstName("Rahul");
            request.setLastName("Agrawal");
            request.setPhoneNumber("7890562300");
            request.setEmail("pankaj@gmail.com");
            request.setAddressLine1("dhsgd");
            request.setAddressLine2("sdgsdhd");
            request.setCity("jaipur");
            request.setState("rajasthan");
            request.setZipcode("302029");
            request.setAge("25");
            request.setPassportNumber("L74568900");
            MockMultipartFile imageFile = new MockMultipartFile("aadhaarImageFile", "aadhaar4.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());
            request.setPassportDoc(imageFile);
            String extractedText = "Y894670RahulAgrawal";
            BufferedImage bufferedImage=null;
//            Mockito.when(passportDataRepository.save(any())).thenReturn(new PassportData());
//            Mockito.when(addressRepository.save(any())).thenReturn(new Address());
            Mockito.when(tesseract.doOCR(bufferedImage)).thenReturn(extractedText);
            when(validationService.verifyValidation(request)).thenReturn(true);
            when(passportDataRepository.findByNo(anyString())).thenReturn(null);
            VerificationResponse response = passportService.registerPassport(request);
            assertEquals("Passport Number does not match", response.getMessage());
        }
        @Test
        public void testRegisterPassport_RegisteredAlready() throws TesseractException, IOException, ValidationException {
            PassportDataRequest request = new PassportDataRequest();
            request.setFirstName("Pankaj");
            request.setLastName("Sharma");
            request.setPhoneNumber("7890562300");
            request.setEmail("pankaj@gmail.com");
            request.setAddressLine1("dhsgd");
            request.setAddressLine2("sdgsdhd");
            request.setCity("jaipur");
            request.setState("rajasthan");
            request.setZipcode("302029");
            request.setAge("25");
            request.setPassportNumber("F27284823");

            MockMultipartFile imageFile = new MockMultipartFile("aadhaarImageFile", "aadhaar4.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());
            request.setPassportDoc(imageFile);
            String extractedText = "F27284823PankajSharma";
            BufferedImage bufferedImage=null;
//            Mockito.when(passportDataRepository.save(any())).thenReturn(new PassportData());
            Mockito.when(tesseract.doOCR(bufferedImage)).thenReturn(extractedText);
            when(validationService.verifyValidation(request)).thenReturn(true);
            when(passportDataRepository.findByNo(any())).thenReturn(new PassportData());
            VerificationResponse response = passportService.registerPassport(request);
            assertEquals("Registered Already", response.getMessage());
        }
        @Test
        public void testRegisterPassport_ValidationFailure() throws TesseractException, IOException, ValidationException {

            PassportDataRequest request = new PassportDataRequest();
            request.setFirstName("Pan637");
            request.setLastName("Sharma32");
            request.setPhoneNumber("78905cj62300");
            request.setEmail("pankaj.gmail.com");
            request.setAddressLine1("dhsgd");
            request.setAddressLine2("sdgsdhd");
            request.setCity("jaipur");
            request.setState("rajasthan");
            request.setZipcode("302029");
            request.setAge("25");
            request.setPassportNumber("F27284823");
            InputStream inputStream = mock(InputStream.class);
            MockMultipartFile mockMultipartFile = new MockMultipartFile("passportDoc", "filename.txt", "text/plain", inputStream);
            request.setPassportDoc(mockMultipartFile);
            Address address=new Address();
            address.setZipcode("74843");
            address.setAddressLine2("dhsajds");
            address.setAddressLine1("shsdd");
            address.setId(address.getId());

            address.setState("sdsds");
            address.setCity("hdsjd");
            PassportData passportData=new PassportData();
            passportData.setFirstName("hdjdd");
            passportData.setLastName("sshdhsd");
            passportData.setEmail("dhsadsa@jdd.dds");
            passportData.setPassportNumber("H3828392");
            passportData.setAddress(address);
            passportData.setPhoneNumber("2832982323");
            passportData.setAge("34");
            passportData.setId(passportData.getId());
            passportData.getPassportNumber();
            passportData.getAge();
            passportData.getFirstName();
            passportData.getLastName();
            passportData.getAddress();
            passportData.getPhoneNumber();
            passportData.getEmail();
            address.getCity();
            address.getState();
            address.getAddressLine1();
            address.getAddressLine2();
            address.getZipcode();
            address.getId();
            GetRequest getRequest=new GetRequest("hjdd");

            VerificationResponse verificationResponse=new VerificationResponse();
            verificationResponse.setMessage(null);
            VerificationResponse verificationResponse1=new VerificationResponse(null);

            when(validationService.verifyValidation(request)).thenReturn(false);
            verify(tesseract, never()).doOCR((File) any());
            verify(passportDataRepository, never()).findByNo(anyString());

            ValidationException exception = assertThrows(ValidationException.class,
                    () -> passportService.registerPassport(request));



            assertEquals("Provided input is syntax incorrect", exception.getMessage());

        }
        @Test
        void testGetPassport_Success() throws PassportException {

            String email = "test@example.com";
            GetRequest getRequest = new GetRequest();
            getRequest.setEmail(email);

            PassportData expectedPassportData = new PassportData();
            expectedPassportData.setEmail(email);

            when(passportDataRepository.findByEmail(email)).thenReturn(expectedPassportData);


            PassportData result = passportService.getPassport(getRequest);


            assertEquals(expectedPassportData, result);
            verify(passportDataRepository, times(1)).findByEmail(email);
        }

        @Test
        void testGetPassport_PassportNotFound() {

            String email = "nonexistent@example.com";
            GetRequest getRequest = new GetRequest();
            getRequest.setEmail(email);

            when(passportDataRepository.findByEmail(email)).thenReturn(null);


            PassportException exception = assertThrows(PassportException.class, () -> {
                passportService.getPassport(getRequest);
            });

            assertEquals("Error Fetching the details", exception.getMessage());
            verify(passportDataRepository, times(1)).findByEmail(email);
        }
        @Test
        void testPassportExceptionMessage() {

            String errorMessage = "This is an error message.";


            PassportException passportException = new PassportException(errorMessage);


            assertEquals(errorMessage, passportException.getMessage());
        }

        @Test
        void testPassportExceptionWithNullMessage() {

            PassportException passportException = new PassportException(null);


            assertEquals(null, passportException.getMessage());
        }




    @Test
    void testRegisterPassport_TesseractException() throws TesseractException, IOException, ValidationException {
        PassportDataRequest request = new PassportDataRequest();
        request.setFirstName("Rahul");
        request.setLastName("Agrawal");
        request.setPhoneNumber("7890562300");
        request.setEmail("pankaj@gmail.com");
        request.setAddressLine1("dhsgd");
        request.setAddressLine2("sdgsdhd");
        request.setCity("jaipur");
        request.setState("rajasthan");
        request.setZipcode("302029");
        request.setAge("25");
        request.setPassportNumber("L74568900");
        MockMultipartFile imageFile = new MockMultipartFile("aadhaarImageFile", "aadhaar4.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());
        request.setPassportDoc(imageFile);
        String extractedText = "F27284823RahulSharma";
        BufferedImage bufferedImage=null;
        when(validationService.verifyValidation(request)).thenReturn(true);
        when(passportDataRepository.findByNo(any())).thenReturn(null);
        when(tesseract.doOCR(bufferedImage)).thenThrow(new TesseractException("Test TesseractException"));


        assertThrows(TesseractException.class, () -> passportService.registerPassport(request));


        verify(passportDataRepository, never()).save(any());
        verify(addressRepository, never()).save(any());
    }
}