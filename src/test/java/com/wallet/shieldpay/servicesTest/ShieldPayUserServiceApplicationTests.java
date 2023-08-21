package com.wallet.shieldpay.servicesTest;

import com.wallet.shieldpay.dto.requests.ChangePasswordRequest;
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
import com.wallet.shieldpay.repositories.UserRepository;
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
    private UserService userService;
    @Autowired
    private WalletService walletService;
    @Autowired
    UserRepository userRepository;
    private  SignUpRequest signUpRequestForLoggedInUser;
    private SignUpRequest setUpSignUpRequest;
    private SignUpRequest signUpRequest;
    private SignUpResponse setUpSignUpResponse;
    private SignUpResponse signUpResponseForLoggedInUser;
    private SignUpResponse signUpResponse ;
    private LoginResponse loginResponse;

    private String password ;
    private String email ;
    private User foundUser ;
    @Autowired
    OTPService otpService;
    static int signUpWithConfirmationCounter = 0;

    static int forgetPasswordCount = 0;
    static int otpIsDeletedOnceConfirmed = 0;
    static int walletCreatedOnConfirmation = 0;


    @BeforeEach
    void setUp(){
        setUpSignUpRequest = createSignUpRequest("setupuser@gmail.com","09062027236");
        try {
            userServiceSignUp(setUpSignUpRequest,setUpSignUpResponse);

        }catch (UserAlreadyExistException e) {
            log.error("User set up log catch  block failed {}", "----- {}".repeat(5), e.getMessage());

        }
        setFoundUserFoundByEmail(setUpSignUpRequest.getEmail());
        assertNotNull(foundUser);

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

        signUpRequestForLoggedInUser = SignUpRequest.builder()
                .firstName("logged_in")
                .lastName("logged-in")
                .password("P@ssw0rd")
                .email("loggeinuser@gmail.com")
                .phoneNumber("09062028394")
                .build();


        try {
            createALoggedInUserTryBlock();
        }catch (UserAlreadyExistException exception) {
            createALoggedInUserCatchBlock(exception);
        }
        finally {
            createALoggedInUserFinallyBlock();
        }

    }
    private void createALoggedInUserTryBlock(){
        userServiceSignUp(signUpRequestForLoggedInUser,signUpResponseForLoggedInUser);
        setEmailAndPasswordVariables(
                signUpRequestForLoggedInUser.getEmail(),
                signUpRequestForLoggedInUser.getPassword()
        );
        setFoundUserFoundByEmail(signUpRequestForLoggedInUser.getEmail());
    }
    private void createALoggedInUserCatchBlock(Exception exception) {
        log.error("create A Logged In User catch block {}", "----- {}".repeat(3), exception.getMessage());
        findAndDeleteUser(signUpRequestForLoggedInUser.getEmail());
        userServiceSignUp(signUpRequestForLoggedInUser, signUpResponseForLoggedInUser);
        setEmailAndPasswordVariables(
                signUpRequestForLoggedInUser.getEmail(),
                signUpRequestForLoggedInUser.getPassword()
        );
    }
    private void createALoggedInUserFinallyBlock(){
        String otp = userService.getMockOtp();
        assertTrue(otpService.isExisting(otp));

        assertEquals(foundUser.getEmail(), signUpRequestForLoggedInUser.getEmail());

        SignUpConfirmationResponse signUpConfirmedResponse = userService.userOTPCodeConfirmation(otp);

        assertEquals(signUpConfirmedResponse.getEmail(), signUpRequestForLoggedInUser.getEmail());

        assertTrue(signUpConfirmedResponse.isConfirmedUser());

        userService.userOTPCodeConfirmation(otp);

        loginResponse = userService.login(email, password);
    }
    private void findAndDeleteUser(String email){
        setFoundUserFoundByEmail(email);
        userRepository.delete(foundUser);
    }
    private void userServiceSignUp(SignUpRequest request, SignUpResponse response) {
        response = userService.signUp(request);
    }
    private SignUpResponse userServiceSignUp(SignUpRequest request) {
        return userService.signUp(request);
    }
    private void setFoundUserFoundByEmail(String email){
        foundUser = userService.findUserByEmail(email);
    }
    private void setEmailAndPasswordVariables(String email, String password){
        this.email = email;
        this.password = password;
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

        try {
            signUpResponse = userService.signUp(signUpRequest);

        }catch (UserAlreadyExistException e) {
            log.error( "testUserCanSignUpButNeedConfirmation --- --- ----- ----- {}".repeat(10), e.getMessage());
        }
        assertNotNull(signUpResponse);

        assertFalse(signUpResponse.isConfirmedUser());

        assertNotNull(signUpResponse.getDateRegistered());
    }
    @Test
    void onSignUpOtpIsSent() {
        signUpRequest = createSignUpRequest("otpissent@gmail.com","07533565836");


        try {
            signUpResponse =  userServiceSignUp(signUpRequest);

        }catch (UserAlreadyExistException e) {
            log.error( "onSignUpOtpIsSent --- --- ----- ----- {}".repeat(7), e.getMessage());
            setFoundUserFoundByEmail(signUpRequest.getEmail());
            findAndDeleteUser(foundUser.getEmail());
            userServiceSignUp(signUpRequest, signUpResponse);
        }

        String message ="An OTP code has been sent to " +
                signUpRequest.getEmail() + ". Please do not share this code with others";

        assertNotNull(signUpResponse);
        setFoundUserFoundByEmail(signUpRequest.getEmail());
        assertFalse(foundUser.isConfirmedUser());
        assertNotNull(foundUser.getDateRegistered());
        assertEquals(signUpResponse.getMessage(), message);
        assertEquals(signUpResponse.getEmail(), foundUser.getEmail());

        assertFalse(foundUser.isConfirmedUser());
        assertTrue(signUpResponse.isOtpSent());
    }



    @Test
    void testUserCanSignUpWithConfirmation() {
        signUpRequest =createSignUpRequest("userCanSignUpWithConfirmation@gmail.com","09045674275");

        if (signUpWithConfirmationCounter == 0) {
            setUpSignUpResponse = userService.signUp(signUpRequest);
            signUpWithConfirmationCounter++;
        }
        setFoundUserFoundByEmail(setUpSignUpRequest.getEmail());
        foundUser = userService.findUserByEmail(signUpRequest.getEmail());

        String OTP = userService.getMockOtp();

        assertEquals(foundUser.getEmail(), signUpRequest.getEmail());

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
        setFoundUserFoundByEmail(setUpSignUpRequest.getEmail());
       assertNotNull(foundUser);

        assertThrows(  UserAlreadyExistException.class,()-> userService.signUp(setUpSignUpRequest));
    }
    @Test
    void testUserCanLogin(){

        String email = signUpRequestForLoggedInUser.getEmail();
        String password = signUpRequestForLoggedInUser.getPassword();

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
    @Test
    void checkThatUserCanChangePassword(){
        setFoundUserFoundByEmail(setUpSignUpRequest.getEmail());
        String email = foundUser.getEmail();
        String oldPassword = foundUser.getPassword();
        String newPassword = "W0rdp@ss";

        ChangePasswordRequest changeRequest = new ChangePasswordRequest();
        changeRequest.setEmail(email);
        changeRequest.setOldPassword(oldPassword);
        changeRequest.setNewPassword(newPassword);


        var response = userService.changePassword(changeRequest);
        assertEquals(response.getMessage() , "Password Change Successful");

    }
}
