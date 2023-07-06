package com.wallet.shieldpay.services.serviceInterface;

import com.wallet.shieldpay.models.UtilityModels.OTP;

public interface OTPService {

     String generateOTP(String email);
    void deleteOTP(OTP otp);
    boolean confirmOTP( String email, String otp);
    boolean verifyIsActiveOTP(String email, String otp);
//    OTP findOTPByCode(
}
