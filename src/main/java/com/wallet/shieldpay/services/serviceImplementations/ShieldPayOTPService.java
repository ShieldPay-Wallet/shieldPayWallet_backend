package com.wallet.shieldpay.services.serviceImplementations;

import com.wallet.shieldpay.models.User;
import com.wallet.shieldpay.models.UtilityModels.OTP;
import com.wallet.shieldpay.models.UtilityModels.OTPType;
import com.wallet.shieldpay.repositories.OTPRepository;
import com.wallet.shieldpay.services.serviceInterface.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.OTPGeneratorImplementation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShieldPayOTPService implements OTPService {
    @Autowired
    OTPRepository otpRepository;
    /**
     * @return
     */
    @Override
    public String generateOTP(String email) {
        String otp = OTPGeneratorImplementation.generate();
        LocalTime localTime = LocalTime.now();
        OTP createdOtp = OTP.builder()
                .isActiveOtp(true)
                .email(email)
                .otp(otp)
                .dateCreated( LocalDate.now())
                .timeCreatedInMinute(localTime.getMinute())
                .build();

        otpRepository.save(createdOtp);

         return createdOtp.getOtp();
    }
    public String generateOTP(String email, String otp, OTPType otpType){
        LocalTime localTime = LocalTime.now();

        OTP createdOtp = OTP.builder()
                .isActiveOtp(true)
                .email(email)
                .otp(otp)
                .dateCreated( LocalDate.now())
                .timeCreatedInMinute(localTime.getMinute())
                .otpType(otpType)
                .build();

        otpRepository.save(createdOtp);

        return createdOtp.getOtp();
    }

    /**
     * @param otp
     */
    @Override
    public void deleteOTP(OTP otp) {
        otpRepository.delete(otp);
    }

    /**
     * @param email
     * @param otp
     * @return
     */
    @Override
    public boolean confirmOTP(String email, String otp) {
        Optional<OTP> userOtp = otpRepository.findByOtp(otp);
        return userOtp != null;
    }




    /**
     * @param otp
     * @return
     */
    @Override
    public OTP confirmOTP(String otp) {

        OTP userOtp = null;
        Optional<OTP> userOtpOption = otpRepository.findByOtp(otp);


        if (userOtpOption.isPresent()) {
             userOtp = userOtpOption.get();

                deleteOTP(userOtp);

        }
        return userOtp;
    }

    /**
     * @paramotp
     * @return
     */
    @Override
    public boolean isExisting(String otpToCheckFor) {
        List<OTP> allOtp = otpRepository.findAll();
        for (OTP otp : allOtp) {
            if (otp.getOtp().equals(otpToCheckFor)){
                return true;
            }
        }
        return false;
    }
}
