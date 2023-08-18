package com.wallet.shieldpay.services.serviceImplementations;


import com.wallet.shieldpay.dto.requests.ResetPasswordRequest;
import com.wallet.shieldpay.exceptions.PasswordMismatchException;
import com.wallet.shieldpay.models.User;
import com.wallet.shieldpay.repositories.UserRepository;
import com.wallet.shieldpay.services.serviceInterface.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
        @Autowired
        UserRepository userRepository;

        @Override
        public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest) {
            Optional<User> optionalUser = userRepository.findUserByEmail(
                            resetPasswordRequest.getEmail());
            User foundCustomer = null;
            if (optionalUser.isPresent()) {
                foundCustomer = optionalUser.get();
            }

            if (!Objects.equals(resetPasswordRequest.getNewPassword(), resetPasswordRequest.getConfirmPassword()))
                throw new PasswordMismatchException("Password is not a match");
            foundCustomer.setPassword(resetPasswordRequest.getNewPassword());
            userRepository.save(foundCustomer);

            return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
        }

    }