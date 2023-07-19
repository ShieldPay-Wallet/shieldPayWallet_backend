package com.wallet.shieldpay.repositories;

import com.wallet.shieldpay.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long>{
}
