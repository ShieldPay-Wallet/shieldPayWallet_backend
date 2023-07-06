package com.wallet.shieldpay.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {
    @Id
    private Long userId;
    private String WalletId;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private String walletAccountNumber;

    private boolean isActive;
    private boolean isConfirmedUser;

    private Date dateRegistered;
    private Date dateConfirmed;
}
