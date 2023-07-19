package com.wallet.shieldpay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {
    private String email;
    private String message;
    private boolean isConfirmedUser;

    private LocalDate dateRegistered = LocalDate.now();
    private boolean isOtpSent;

}
