package com.wallet.shieldpay.servicesTest;

import com.wallet.shieldpay.dto.requests.SignUpRequest;
import com.wallet.shieldpay.dto.requests.WalletCreationRequest;
import com.wallet.shieldpay.dto.response.SignUpResponse;
import com.wallet.shieldpay.exceptions.UserAlreadyExistException;

import com.wallet.shieldpay.models.Wallet;
import com.wallet.shieldpay.services.serviceInterface.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;
@Slf4j
public class WalletServiceTest {

    @Autowired
    private WalletService walletService;
    private WalletCreationRequest walletCreationRequest;
    private Wallet foundWallet;

    @BeforeEach
    void setUp(){
        walletCreationRequest = createWalletRequest("9062027936", "1234");
        try {
            walletService.createNewWallet(walletCreationRequest);

        }catch (Exception e) {
            log.error("User set up log catch  block failed {}", "----- {}".repeat(5), e.getMessage());

        }
        assertNotNull(foundWallet);

    }
    private void userServiceSignUp(SignUpRequest request, SignUpResponse response) {
        response = userService.signUp(request);
    }

    private WalletCreationRequest createWalletRequest( String accountNumber, String pin){
        Long userId = null;
        return WalletCreationRequest.builder()
                .accountNumber(accountNumber)
                .userId(userId)
                .build();
    }
}
