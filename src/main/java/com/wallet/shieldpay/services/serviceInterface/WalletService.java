package com.wallet.shieldpay.services.serviceInterface;

import com.wallet.shieldpay.dto.requests.WalletCreationRequest;
import com.wallet.shieldpay.models.Wallet;

public interface WalletService {

    Wallet createNewWallet(WalletCreationRequest walletCreationRequest);
}
