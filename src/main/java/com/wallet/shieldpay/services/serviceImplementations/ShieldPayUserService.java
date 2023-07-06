package com.wallet.shieldpay.services.serviceImplementations;

import com.wallet.shieldpay.dto.requests.SignUpRequest;
import com.wallet.shieldpay.dto.response.SignUpResponse;
import com.wallet.shieldpay.models.User;
import com.wallet.shieldpay.models.UtilityModels.EmailCreator;
import com.wallet.shieldpay.services.serviceInterface.MailSenderServiceInterface;
import com.wallet.shieldpay.services.serviceInterface.OTPService;
import com.wallet.shieldpay.services.serviceInterface.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ShieldPayUserService implements UserService {

//    UserRepository userRepository;
    @Autowired
    private MailSenderServiceInterface emailSenderService;
    @Autowired
    OTPService otpService;

    /**
     * @param signUpRequest
     * @return
     */
    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
       User user = new User();
        BeanUtils.copyProperties(signUpRequest, user);

        SignUpResponse signUpResponse = new SignUpResponse();
       BeanUtils.copyProperties(user, signUpResponse);

        emailSenderService.sendSimpleMail(
                        EmailCreator.builder()
                                .receiverEmail(user.getEmail())
                                .subject("Shield Pay Secure SignUp")
                                .body(otpService.generateOTP(user.getEmail()))
                                .build()
                        );

       signUpResponse.setMessage(
               "An OTP code has been sent to " +
               user.getEmail() + ". Please do not share this code with others"
                );
        signUpResponse.setOtpSent(true);

       return signUpResponse;
    }

    /**
     * @param email
     * @return
     */
    @Override
    public User findUserByEmail(String email) {
        return null;
    }




    /**
     * @param otp
     * @return
     */
    @Override
    public SignUpResponse userOTPCodeConfirmation(int otp) {
        return null;
    }



//    @EventListener(ApplicationReadyEvent.class)
//    public void emailSender(String email){
//        String otpToUse = otpService.generateOTP();
//
//        emailSenderService.sendSimpleMail(EmailCreator.builder()
//                .emailToSendTo(email)
//                .subject("Your OTP is should not be shown to anyone. pls kept it private." +
//                        "\n from Shield PaY")
//                .body(otpToUse)
//                .build()
//        );
//    }
}
