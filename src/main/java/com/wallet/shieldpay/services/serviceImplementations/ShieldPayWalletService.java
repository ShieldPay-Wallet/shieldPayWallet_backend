package com.wallet.shieldpay.services.serviceImplementations;

import com.wallet.shieldpay.dto.requests.CheckBalanceRequest;
import com.wallet.shieldpay.dto.requests.DepositRequest;
import com.wallet.shieldpay.dto.requests.WalletCreationRequest;
import com.wallet.shieldpay.models.Wallet;
import com.wallet.shieldpay.repositories.WalletRepository;
import com.wallet.shieldpay.services.serviceInterface.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ShieldPayWalletService implements WalletService {
    @Autowired
    WalletRepository walletRepository;
    /**
     * @param walletCreationRequest
     */
    @Override
    public Wallet createNewWallet(WalletCreationRequest walletCreationRequest ){
        Wallet wallet = new Wallet();

        wallet.setAccountNumber(walletCreationRequest.getAccountNumber());
        wallet.setUserId(wallet.getUserId());

        Wallet savedWallet = walletRepository.save(wallet);

        return savedWallet;
    }

    /**
     * @param checkBalanceRequest
     * @return
     */
    @Override
    public BigDecimal checkBalance(CheckBalanceRequest checkBalanceRequest) {
        return new BigDecimal(0.0);
    }

    /**
     * @param depositRequest
     */
    @Override
    public void deposit(DepositRequest depositRequest) {

    }
}
