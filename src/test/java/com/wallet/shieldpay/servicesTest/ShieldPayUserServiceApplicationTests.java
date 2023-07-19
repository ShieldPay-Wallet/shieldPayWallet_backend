package com.wallet.shieldpay.servicesTest;

import com.wallet.shieldpay.dto.requests.SignUpRequest;
import com.wallet.shieldpay.dto.response.ForgotPasswordResponse;
import com.wallet.shieldpay.dto.response.LoginResponse;
import com.wallet.shieldpay.dto.response.SignUpConfirmationResponse;
import com.wallet.shieldpay.dto.response.SignUpResponse;
import com.wallet.shieldpay.exceptions.EmailAlreadyExistException;
import com.wallet.shieldpay.exceptions.InValidEmailException;

import com.wallet.shieldpay.models.User;
import com.wallet.shieldpay.services.serviceInterface.OTPService;
import com.wallet.shieldpay.services.serviceInterface.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ShieldPayUserServiceApplicationTests {
    @Autowired
    private UserService userService;
    private SignUpRequest signUpRequest;
    SignUpResponse signUpResponse;
    @Autowired
    OTPService otpService;
    static int setUpCounter = 0;
    static int needConfirmation = 0;
    static int OtpIsSent = 0;
    static int forgetPasswordCount = 0;
    static int otpIsDeletedOnceConfirmed = 0;
    static int walletCreatedOnConfirmation = 0;

    User user = null;
    @BeforeEach
    void setUp(){
        signUpRequest = SignUpRequest.builder()
                .firstName("Ned")
                .lastName("Stark")
                .password("P@ssw0rd")
                .email("osisiogubenjamin1@gmail.com")
                .phoneNumber("09069057236")
                .build();
        if (setUpCounter == 0) {
            signUpResponse = userService.signUp(signUpRequest);
            setUpCounter++;
        }
        user = userService.findUserByEmail(signUpRequest.getEmail());
    }

    @Test
    void unRegisteredUserCanNotBeFound(){
        signUpRequest = SignUpRequest.builder()
                .firstName("Ned")
                .lastName("Stark")
                .password("P@ssw0rd")
                .email("osisiogu@gmail.com")
                .phoneNumber("09069057236")
                .build();
        assertNull(userService.findUserByEmail(signUpRequest.getEmail()));
    }
    @Test
    void testUserCanSignUpButNeedConfirmation() {
        signUpRequest = SignUpRequest.builder()
                .firstName("Ned")
                .lastName("Stark")
                .password("P@ssw0rd")
                .phoneNumber("08133567834")
                .email("favourtwo@gmail.com")

                .build();
        SignUpResponse signUpResponse = null;
        if (needConfirmation == 0) {
         signUpResponse = userService.signUp(signUpRequest);
            needConfirmation++;
        }

        assertNotNull(signUpResponse);

        assertFalse(signUpResponse.isConfirmedUser());

        assertNotNull(signUpResponse.getDateRegistered());
    }
    @Test
    void onSignUpOtpIsSent() {
        signUpRequest = SignUpRequest.builder()
                .firstName("Ned")
                .lastName("Stark")
                .password("P@ssw0rd")
                .phoneNumber("08533565836")
                .email("otpissent@gmail.com")
                .build();
        SignUpResponse signUpResponse = null;
        if (OtpIsSent == 0) {
            signUpResponse = userService.signUp(signUpRequest);
            OtpIsSent++;
        }

        String message ="An OTP code has been sent to " +
                signUpRequest.getEmail() + ". Please do not share this code with others";

//        assertNotNull(signUpResponse);

        assertFalse(user.isConfirmedUser());
        assertNotNull(user.getDateRegistered());
        assertEquals(signUpResponse.getMessage(), message);

        assertFalse(user.isConfirmedUser());
        assertTrue(signUpResponse.isOtpSent());
    }
    @Test
    void testUserCanSignUpWithConfirmation() {

        String OTP = userService.getMockOtp();

        assertEquals(user.getEmail(), signUpRequest.getEmail());

        SignUpConfirmationResponse signUpConfirmedResponse = userService.userOTPCodeConfirmation(OTP);

         assertEquals(signUpConfirmedResponse.getEmail(), signUpRequest.getEmail());

        assertTrue(signUpConfirmedResponse.isConfirmedUser());
    }
    @Test
    void testThatUserCannotSignUpWithInvalidEmail() {
        signUpRequest = SignUpRequest.builder()
                .firstName("Ned")
                .lastName("Stark")
                .password("P@ssw0rd")
                .email("example.com")
                .build();

        assertThrows(  InValidEmailException.class,()-> userService.signUp(signUpRequest));
    }
    @Test
    void testThatUserCannotSignUpWithInvalidPassword() {
        signUpRequest = SignUpRequest.builder()
                .firstName("Ned")
                .lastName("Stark")
                .password("Pfdehk")
                .email("uniqueexample@example.com")
                .build();

//        assertThrows(  InValidPasswordException.class,()-> userService.signUp(signUpRequest));
    }
    @Test
    void testThatUserCannotSignUpWithTheSameEmail() {
        signUpRequest = SignUpRequest.builder()
                .firstName("John")
                .lastName("wick")
                .password("P@ssw0rd")
                .email("osisiogubenjamin1@gmail.com")
                .build();

        assertThrows(  EmailAlreadyExistException.class,()-> userService.signUp(signUpRequest));
    }
    @Test
    void testUserCanLogin(){

        String OTP = userService.getMockOtp();
        assertEquals(user.getEmail(), signUpRequest.getEmail());

        SignUpConfirmationResponse signUpConfirmedResponse = userService.userOTPCodeConfirmation(OTP);

        assertEquals(signUpConfirmedResponse.getEmail(), signUpRequest.getEmail());

        assertTrue(signUpConfirmedResponse.isConfirmedUser());

        String email = user.getEmail();
        String password = signUpRequest.getPassword();

        LoginResponse loginResponse = userService.login(email, password);

        assertEquals(loginResponse.getEmail(), email);
        assertTrue(loginResponse.isConfirmedUser());
        assertTrue(loginResponse.isActive());
    }
    @Test
    public void testForgotPasswordMethod(){
        signUpRequest = SignUpRequest.builder()
                .firstName("Ned")
                .lastName("Stark")
                .password("P@ssw0rd")
                .phoneNumber("08533565836")
                .email("forgetPassword@gmail.com")
                .build();

        if (forgetPasswordCount == 0) {
             userService.signUp(signUpRequest);
            forgetPasswordCount++;
        }


        String OTP = userService.getMockOtp();


        SignUpConfirmationResponse signUpConfirmedResponse = userService.userOTPCodeConfirmation(OTP);

        assertEquals(signUpConfirmedResponse.getEmail(), signUpRequest.getEmail());

        assertTrue(signUpConfirmedResponse.isConfirmedUser());

        String email = signUpRequest.getEmail();

        ForgotPasswordResponse forgotPasswordResponse = userService.forgotPassword(email);

        String message ="An OTP code has been sent to " +
                signUpRequest.getEmail() + ". Please do not share this code with others";




    }
    @Test
    public void otpIsDeletedAfterConfirmation() {
        signUpRequest = SignUpRequest.builder()
                .firstName("Ned")
                .lastName("Stark")
                .password("P@ssw0rd")
                .phoneNumber("08533565836")
                .email("deletedAfterconfirmation@gmail.com")

                .build();

        if (otpIsDeletedOnceConfirmed == 0) {
            userService.signUp(signUpRequest);
            otpIsDeletedOnceConfirmed++;
        }
        String otp = userService.getMockOtp();

        assertTrue(otpService.isExisting(otp));
        userService.userOTPCodeConfirmation(otp);

        assertFalse(otpService.isExisting(otp));

    }
    @Test
    public void testWalletIsCreatedOnceUserIsConfirmed(){
        signUpRequest = SignUpRequest.builder()
                .firstName("Ned")
                .lastName("Stark")
                .password("P@ssw0rd")
                .phoneNumber("08533565836")
                .email("walletCreatedOnConfirmation@gmail.com")

                .build();

        if (walletCreatedOnConfirmation == 0) {
            userService.signUp(signUpRequest);
            walletCreatedOnConfirmation++;
        }
        String otp = userService.getMockOtp();
        User user = userService.findUserByEmail(signUpRequest.getEmail());

        assertNull(user.getWalletId());

        userService.userOTPCodeConfirmation(otp);

        User confirmedUser = userService.findUserByEmail(signUpRequest.getEmail());
        assertNotNull(confirmedUser.getWalletId());
    }

}
