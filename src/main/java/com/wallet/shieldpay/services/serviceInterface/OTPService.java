package com.wallet.shieldpay.services.serviceInterface;

import com.wallet.shieldpay.dto.requests.OtpCreationRequest;
import com.wallet.shieldpay.models.UtilityModels.OTP;
import com.wallet.shieldpay.models.UtilityModels.OTPType;

public interface OTPService {

     String generateOTP(String email);
    public String generateOTP(OtpCreationRequest otpCreationRequest);
    void deleteOTP(OTP otp);
    boolean confirmOTP( String email, String otp);

    OTP confirmOTP(String otp);

    boolean isExisting(String otp);
//    OTP findOTPByCode(
}
