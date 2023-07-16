package com.wallet.shieldpay.dto.requests;

import com.wallet.shieldpay.models.UtilityModels.OTPType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpCreationRequest {
    private String email;
    private String otp;
    private OTPType otpType;

}
