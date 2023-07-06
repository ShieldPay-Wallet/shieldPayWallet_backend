package com.wallet.shieldpay;

import com.wallet.shieldpay.dto.requests.SignUpRequest;
import com.wallet.shieldpay.dto.response.SignUpResponse;
import com.wallet.shieldpay.services.serviceInterface.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ShieldPayApplicationTests {
    @Autowired
    private UserService userService;
    private SignUpRequest signUpRequest;

    @Test
    void unRegisteredUserCanNotBeFound(){
        signUpRequest = SignUpRequest.builder()
                .firstName("Ned")
                .lastName("Stark")
                .password("P@ssw0rd")
                .email("example@example.com")
                .build();
        assertNull(userService.findUserByEmail(signUpRequest.getEmail()));
    }
    @Test
    void testUserCanSignUpButNeedConfirmation() {
        signUpRequest = SignUpRequest.builder()
                .firstName("Ned")
                .lastName("Stark")
                .password("P@ssw0rd")
                .email("example@example.com")
                .build();
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);

        assertNotNull(signUpResponse);

        assertFalse(signUpResponse.isConfirmedUser());
        assertNull(signUpResponse.getWalletAccountNumber());
        assertNull(userService.findUserByEmail(signUpRequest.getEmail()));
        assertNotNull(signUpResponse.getDateRegistered());
    }
    @Test
    void onSignUpOtpIsSent() {
        signUpRequest = SignUpRequest.builder()
                .firstName("Ned")
                .lastName("Stark")
                .password("P@ssw0rd")
                .email("example@example.com")
                .build();
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        String message ="An OTP code has been sent to " +
                "example@example.com." + " Please do not share this code with others";

        assertNotNull(signUpResponse);

        System.out.println(signUpResponse.getDateRegistered());
        assertFalse(signUpResponse.isConfirmedUser());
        assertNull(signUpResponse.getWalletAccountNumber());
        assertNull(userService.findUserByEmail(signUpRequest.getEmail()));
        assertNotNull(signUpResponse.getDateRegistered());
        assertEquals(signUpResponse.getMessage(), message);

        assertFalse(signUpResponse.isConfirmedUser());
        assertTrue(signUpResponse.isOtpSent());
    }
    @Test
    void testUserCanSignUpWithConfirmation() {
        signUpRequest = SignUpRequest.builder()
                .firstName("Ned")
                .lastName("Stark")
                .password("P@ssw0rd")
                .email("example@example.com")
                .build();
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);


        int OTP = 1234;

        assertEquals(signUpResponse.getFirstName(), signUpRequest.getFirstName());
        assertEquals(signUpResponse.getLastName(), signUpRequest.getLastName());
        assertEquals(signUpResponse.getEmail(), signUpRequest.getEmail());

        assertNull(signUpResponse.getWalletAccountNumber());
        assertNull(userService.findUserByEmail(signUpRequest.getEmail()));

        SignUpResponse signUpConfirmedResponse =userService.userOTPCodeConfirmation(OTP);

        assertEquals(signUpConfirmedResponse.getFirstName(), signUpRequest.getFirstName());
        assertEquals(signUpConfirmedResponse.getLastName(), signUpRequest.getLastName());
        assertEquals(signUpConfirmedResponse.getEmail(), signUpRequest.getEmail());

    }
    @Test
    void contextLoads() {

    }

}
