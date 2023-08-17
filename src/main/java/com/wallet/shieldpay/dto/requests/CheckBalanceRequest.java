package com.wallet.shieldpay.dto.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CheckBalanceRequest {
    private String walletAccountNumber;
    private int pin;
}
