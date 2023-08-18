package com.wallet.shieldpay.servicesTest;

import com.wallet.shieldpay.dto.requests.CheckBalanceRequest;
import com.wallet.shieldpay.dto.requests.DepositRequest;
import com.wallet.shieldpay.dto.requests.SignUpRequest;
import com.wallet.shieldpay.dto.response.ForgotPasswordResponse;
import com.wallet.shieldpay.dto.response.LoginResponse;
import com.wallet.shieldpay.dto.response.SignUpConfirmationResponse;
import com.wallet.shieldpay.dto.response.SignUpResponse;
import com.wallet.shieldpay.exceptions.UserAlreadyExistException;
import com.wallet.shieldpay.exceptions.InValidEmailException;

import com.wallet.shieldpay.exceptions.UserNotFoundException;
import com.wallet.shieldpay.models.User;
import com.wallet.shieldpay.services.serviceImplementations.ShieldPayUserService;
import com.wallet.shieldpay.services.serviceInterface.OTPService;
import com.wallet.shieldpay.services.serviceInterface.UserService;
import com.wallet.shieldpay.services.serviceInterface.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Slf4j
class ShieldPayUserServiceApplicationTests {
    @Autowired
    private UserService userService = new ShieldPayUserService();
    @Autowired
    private WalletService walletService;
    private SignUpRequest setUpSignUpRequest;
    private SignUpRequest signUpRequest;
    private SignUpResponse signUpResponse;
    private SignUpResponse signUpResponseForLoggedInUser;
    private LoginResponse loginResponse;
    @Autowired
    OTPService otpService;
    static int signUpWithConfirmationCounter = 0;
    static int needConfirmation = 0;
    static int OtpIsSent = 0;
    static int forgetPasswordCount = 0;
    static int otpIsDeletedOnceConfirmed = 0;
    static int walletCreatedOnConfirmation = 0;

    User user = null;
    @BeforeEach
    void setUp(){
        setUpSignUpRequest = createSignUpRequest("setupuser@gmail.com","09062027236");
        try {
            signUpResponse = userService.signUp(setUpSignUpRequest);

        }catch (UserAlreadyExistException e) {
            log.error("User set up log catch  block {}", e.getMessage());
        }
        user = userService.findUserByEmail(setUpSignUpRequest.getEmail());

        createALoggedInUser();
    }
    private SignUpRequest createSignUpRequest(String email, String phoneNumber){
       return SignUpRequest.builder()
                .firstName("Ned")
                .lastName("Stark")
                .password("P@ssw0rd")
                .email(email)
                .phoneNumber(phoneNumber)
                .pin(1278)
                .build();
    }

    private void createALoggedInUser(){

        SignUpRequest signUpRequestForLoggedInUser = SignUpRequest.builder()
                .firstName("logged_in")
                .lastName("logged-in")
                .password("P@ssw0rd")
                .email("loggeinuser@gmail.com")
                .phoneNumber("09062028394")
                .build();
        String password = null;
        String email = null;
        try {
            signUpResponseForLoggedInUser = userService.signUp(signUpRequestForLoggedInUser);
                email = signUpRequestForLoggedInUser.getEmail();
              password = signUpRequestForLoggedInUser.getPassword();
        }catch (UserAlreadyExistException e) {
            log.error("create A Logged In User catch block {}", e.getMessage());
            User foundUser = userService.findUserByEmail(signUpRequestForLoggedInUser.getEmail());
            email = foundUser.getEmail();
            password = foundUser.getPassword();

        }
        String otp = userService.getMockOtp();

        assertTrue(otpService.isExisting(otp));
        userService.userOTPCodeConfirmation(otp);

        loginResponse = userService.login(email, password);

    }
    @Test
    void unRegisteredUserCanNotBeFound(){
        signUpRequest = SignUpRequest.builder()
                .email("osisiogu@gmail.com")
                .build();

        assertThrows(UserNotFoundException.class, ()->userService.findUserByEmail(signUpRequest.getEmail()));
    }
    @Test
    void testUserCanSignUpButNeedConfirmation() {
       signUpRequest = createSignUpRequest("favourtwo@gmail.com","08133567834");


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
        signUpRequest = createSignUpRequest("otpissent@gmail.com","07533565836");

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
        signUpRequest =createSignUpRequest("userCanSignUpWithConfirmation@gmail.com","09045674275");

        if (signUpWithConfirmationCounter == 0) {
            signUpResponse = userService.signUp(signUpRequest);
            signUpWithConfirmationCounter++;
        }
        user = userService.findUserByEmail(signUpRequest.getEmail());

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

        assertThrows(  UserAlreadyExistException.class,()-> userService.signUp(signUpRequest));
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
    @Test
    void  testCheckBalance(){

        String walletAccountNumber = loginResponse.getWalletAccountNumber();

        int pin = signUpRequest.getPin();

        CheckBalanceRequest checkBalanceRequest = CheckBalanceRequest.builder()
                .pin(pin)
                .walletAccountNumber(walletAccountNumber)
                .build();

        BigDecimal balance = walletService.checkBalance(checkBalanceRequest);
        assertEquals(balance, new BigDecimal(0.0));
    }
    @Test
    void testDepositCanBeMadeToNewWallet(){
       String walletAccountNumber = loginResponse.getWalletAccountNumber();
        int pin = signUpRequest.getPin();

        CheckBalanceRequest checkBalanceRequest = CheckBalanceRequest.builder()
                .pin(pin)
                .walletAccountNumber(walletAccountNumber)
                .build();

        BigDecimal balance = walletService.checkBalance(checkBalanceRequest);
        assertEquals(balance, new BigDecimal(0.0));

       DepositRequest depositRequest = DepositRequest.builder()
               .walletAccountNumber(walletAccountNumber)
               .amount(new BigDecimal("3000"))
               .SenderName(signUpRequest.getFirstName())
               .build();

       walletService.deposit(depositRequest);


         balance = walletService.checkBalance(checkBalanceRequest);
                assertEquals(balance, new BigDecimal(3000.0));
    }
}
