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
public class SignUpConfirmationResponse {

    private String email;
    private boolean isConfirmedUser;
    private String walletAccountNumber;

}
