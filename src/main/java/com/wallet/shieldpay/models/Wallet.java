package com.wallet.shieldpay.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wallet {
    @Id
    @GeneratedValue
    private Long walletId;

    private Long userId;
    private String accountNumber;

}
