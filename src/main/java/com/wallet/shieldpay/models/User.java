package com.wallet.shieldpay.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long userId;

    private Long WalletId;

    @Column(nullable = false)
    @NotBlank( message = "This field can not be blank")
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
//    @Email("[a-zA-Z0-9]")
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    private String walletAccountNumber;

    private boolean isActive;
    private boolean isConfirmedUser;

    @Column(nullable = false)
    private Date dateRegistered;

    private Date dateConfirmed;
}
