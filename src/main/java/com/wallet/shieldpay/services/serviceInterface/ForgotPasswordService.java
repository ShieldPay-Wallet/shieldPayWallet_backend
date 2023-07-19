package com.wallet.shieldpay.services.serviceInterface;

import com.wallet.shieldpay.dto.requests.ResetPasswordRequest;
import org.springframework.http.ResponseEntity;

public interface ForgotPasswordService {
    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordReq);
}
