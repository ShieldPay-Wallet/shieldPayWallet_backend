package com.wallet.shieldpay.services.serviceInterface;

import com.wallet.shieldpay.dto.requests.SignUpRequest;
import com.wallet.shieldpay.dto.response.SignUpResponse;
import com.wallet.shieldpay.models.User;

public interface UserService {
    SignUpResponse signUp(SignUpRequest signUpRequest);

    User findUserByEmail(String email);

    SignUpResponse userOTPCodeConfirmation(int otp);
}
