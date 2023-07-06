package com.wallet.shieldpay.models.UtilityModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OTP {
    @Id
    private Long otpId;
    private String otp;
    private boolean isActiveOtp;
    private String email;
    private LocalDate dateCreated = LocalDate.now();

}
