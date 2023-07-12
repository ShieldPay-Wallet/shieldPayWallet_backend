package com.wallet.shieldpay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ForgotPasswordResponse {
    private String message;
    private boolean isOtpSent;
}
