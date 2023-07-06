package com.wallet.shieldpay.services.serviceImplementations;

import com.wallet.shieldpay.models.UtilityModels.OTP;
import com.wallet.shieldpay.repositories.OTPRepository;
import com.wallet.shieldpay.services.serviceInterface.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.OTPGenerator;

@Service
public class ShieldPayOTPService implements OTPService {
    @Autowired
    OTPRepository otpRepository;
    /**
     * @return
     */
    @Override
    public String generateOTP(String email) {
        String otp = OTPGenerator.generate();
        OTP createdOtp = OTP.builder()
                .isActiveOtp(true)
                .email(email)
                .otp(otp).build();

        otpRepository.save(createdOtp);

         return createdOtp.getOtp();
    }

    /**
     * @param otp
     */
    @Override
    public void deleteOTP(OTP otp) {

    }

    /**
     * @param email
     * @param otp
     * @return
     */
    @Override
    public boolean confirmOTP(String email, String otp) {
        return false;
    }

    /**
     * @param email
     * @param otp
     * @return
     */
    @Override
    public boolean verifyIsActiveOTP(String email, String otp) {
        return false;
    }
}
