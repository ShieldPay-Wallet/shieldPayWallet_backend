package com.wallet.shieldpay.dto.requests;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DepositRequest {
    private String walletAccountNumber;
    private BigDecimal amount;
    private String SenderName;
}
