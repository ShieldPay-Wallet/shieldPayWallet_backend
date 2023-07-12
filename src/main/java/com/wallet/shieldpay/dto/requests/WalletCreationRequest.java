package com.wallet.shieldpay.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletCreationRequest {
    private String accountNumber;
    private Long userId;
}
