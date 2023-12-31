package com.wallet.shieldpay.services.serviceInterface;

import com.wallet.shieldpay.dto.requests.SignUpRequest;
import com.wallet.shieldpay.dto.response.ForgotPasswordResponse;
import com.wallet.shieldpay.dto.response.LoginResponse;
import com.wallet.shieldpay.dto.response.SignUpConfirmationResponse;
import com.wallet.shieldpay.dto.response.SignUpResponse;
import com.wallet.shieldpay.models.User;

public interface UserService {
     String getMockOtp();
    SignUpResponse signUp(SignUpRequest signUpRequest);

    User findUserByEmail(String email);

    SignUpConfirmationResponse userOTPCodeConfirmation(String otp);

    LoginResponse login(String email, String password);

    ForgotPasswordResponse forgotPassword(String email);
}
