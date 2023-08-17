package com.wallet.shieldpay.services.serviceInterface;

import com.wallet.shieldpay.dto.requests.CheckBalanceRequest;
import com.wallet.shieldpay.dto.requests.DepositRequest;
import com.wallet.shieldpay.dto.requests.WalletCreationRequest;
import com.wallet.shieldpay.models.Wallet;

import java.math.BigDecimal;

public interface WalletService {

    Wallet createNewWallet(WalletCreationRequest walletCreationRequest);
    BigDecimal checkBalance (CheckBalanceRequest checkBalanceRequest);

    void deposit(DepositRequest depositRequest);
}
