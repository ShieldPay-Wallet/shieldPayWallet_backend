package com.wallet.shieldpay.controllersTest;

import com.wallet.shieldpay.controllers.ShieldPayUserController;
import com.wallet.shieldpay.dto.requests.SignUpRequest;
import com.wallet.shieldpay.dto.response.ApiResponse;
import com.wallet.shieldpay.dto.response.SignUpResponse;
import com.wallet.shieldpay.services.serviceInterface.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
//    @Autowired
//    private ShieldPayUserController userController;
//    private SignUpRequest signUpRequest;
//    @Test
//    void testUserCanSignUpButNeedConfirmation() {
//        signUpRequest = SignUpRequest.builder()
//                .firstName("Ned")
//                .lastName("Stark")
//                .password("P@ssw0rd")
//                .email("osisiogubenjamin1@gmail.com")
//                .build();
//        ResponseEntity<?> responseEntity = userController.signUp(signUpRequest);
//
//        SignUpResponse signUpResponse = responseEntity.getBody();
//        assertNotNull(signUpResponse);
//
//        assertFalse(signUpResponse.isConfirmedUser());
//        assertNull(signUpResponse.getWalletAccountNumber());
//        assertNull(userController.findUserByEmail(signUpRequest.getEmail()));
//        assertNotNull(signUpResponse.getDateRegistered());
//    }
}
