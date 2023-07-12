package com.wallet.shieldpay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String message;
    private boolean isConfirmedUser;
    private String walletAccountNumber;
    private boolean isActive;
    }
