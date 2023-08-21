package com.wallet.shieldpay.servicesTest;

import com.wallet.shieldpay.dto.requests.SignUpRequest;
import com.wallet.shieldpay.dto.response.SignUpResponse;
import com.wallet.shieldpay.exceptions.UserAlreadyExistException;
import com.wallet.shieldpay.models.User;
import com.wallet.shieldpay.services.serviceInterface.UserService;
import com.wallet.shieldpay.services.serviceInterface.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;
@Slf4j
public class WalletServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;
    private SignUpRequest setUpSignUpRequest;
    private SignUpResponse setUpSignUpResponse;
    private User foundUser;


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

//        createALoggedInUser();
    }
    private void userServiceSignUp(SignUpRequest request, SignUpResponse response) {
        response = userService.signUp(request);
    }
    private void setFoundUserFoundByEmail(String email){
        foundUser = userService.findUserByEmail(email);
    }
}
