package com.wallet.shieldpay.repositories;

import com.wallet.shieldpay.models.UtilityModels.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findByOtp(String otp);
}
