package com.wallet.shieldpay.services.serviceImplementations;

import com.wallet.shieldpay.dto.requests.OtpCreationRequest;
import com.wallet.shieldpay.dto.requests.SignUpRequest;
import com.wallet.shieldpay.dto.requests.WalletCreationRequest;
import com.wallet.shieldpay.dto.response.ForgotPasswordResponse;
import com.wallet.shieldpay.dto.response.LoginResponse;
import com.wallet.shieldpay.dto.response.SignUpConfirmationResponse;
import com.wallet.shieldpay.dto.response.SignUpResponse;
import com.wallet.shieldpay.exceptions.EmailAlreadyExistException;
import com.wallet.shieldpay.exceptions.InValidEmailException;
import com.wallet.shieldpay.models.User;
import com.wallet.shieldpay.models.UtilityModels.EmailCreator;
import com.wallet.shieldpay.models.UtilityModels.OTP;
import com.wallet.shieldpay.models.Wallet;
import com.wallet.shieldpay.repositories.UserRepository;
import com.wallet.shieldpay.services.serviceInterface.MailSenderServiceInterface;
import com.wallet.shieldpay.services.serviceInterface.OTPService;
import com.wallet.shieldpay.services.serviceInterface.WalletService;
import com.wallet.shieldpay.services.serviceInterface.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.AccountNumberGenerator;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wallet.shieldpay.models.UtilityModels.OTPType.FORGOT_PASSWORD;
import static com.wallet.shieldpay.models.UtilityModels.OTPType.SIGNUP;

@Slf4j
@Service
public class ShieldPayUserService implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private MailSenderServiceInterface emailSenderService;
    @Autowired
    OTPService otpService;

    @Autowired
    WalletService walletService;

    /**
     * @param signUpRequest
     * @return
     */
    int otpCounter = 0;
    public String getMockOtp(){
        String otp = "123" + otpCounter;
        return otp;
    }
    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) throws InValidEmailException {
        boolean isValidPassword = validatePassword(signUpRequest.getPassword());
        boolean isValidEmail = validateEmail(signUpRequest.getEmail());
        boolean isAlreadyUsedEmail =  isAlreadyUsedEmail(signUpRequest.getEmail());

        if (isAlreadyUsedEmail){
            throw new EmailAlreadyExistException("This email already exists ");
        }
            SignUpResponse signUpResponse = new SignUpResponse();

        if(isValidEmail) {
//            if (isValidPassword ){
                User user = new User();
                BeanUtils.copyProperties(signUpRequest, user);
                user.setDateRegistered(new Date());

                BeanUtils.copyProperties(user, signUpResponse);

            OtpCreationRequest otpCreationRequest = OtpCreationRequest.builder()
                    .otp(getMockOtp())
                    .otpType(SIGNUP)
                    .email(user.getEmail())
                    .build();

                otpCounter++;

                emailSenderService.sendSimpleMail(
                        EmailCreator.builder()
                                .receiverEmail(user.getEmail())
                                .subject("Shield Pay Secure SignUp")

                                .body(otpService.generateOTP(otpCreationRequest))
//                                .body(otpService.generateOTP(user.getEmail()))
                                .build()
                );

                signUpResponse.setMessage(
                        "An OTP code has been sent to " +
                                user.getEmail() + ". Please do not share this code with others"
                );
                signUpResponse.setOtpSent(true);
                userRepository.save(user);

//            }else {
//                throw new InValidPasswordException("The password is invalid");
//            }

        }
        else {
            throw new InValidEmailException("email is invalid");
        }
       return signUpResponse;
    }
    /**
     * @param otp
     * @return
     */
    @Override
    public SignUpConfirmationResponse userOTPCodeConfirmation(String otp) {

        OTP userOtp = otpService.confirmOTP(otp);
        SignUpConfirmationResponse signUpConfirmationResponse = new SignUpConfirmationResponse();

        if (userOtp != null) {
            User user = userRepository.findUserByEmail(userOtp.getEmail());

        user.setConfirmedUser(true);
        String phoneNumber = user.getPhoneNumber();
        String accountNumber = AccountNumberGenerator.generateAccountNumber(phoneNumber);

        user.setWalletAccountNumber(accountNumber);
        user.setDateConfirmed(new Date());

        WalletCreationRequest walletCreationRequest = WalletCreationRequest.builder()
                .userId(user.getUserId())
                .accountNumber(accountNumber)
                .build();
        Wallet wallet= walletService.createNewWallet(walletCreationRequest);
        user.setWalletId(wallet.getWalletId());

        userRepository.save(user);

        BeanUtils.copyProperties(user, signUpConfirmationResponse);
        signUpConfirmationResponse.setConfirmedUser(true);

        return signUpConfirmationResponse;
    }
        return signUpConfirmationResponse;
    }

    /**
     * @param email
     * @param password
     * @return
     */
    @Override
    public LoginResponse login(String email, String password) {
        boolean isValidEmail = validateEmail(email);

        LoginResponse loginResponse = new LoginResponse();

        if (isValidEmail){
           User user = userRepository.findUserByEmail(email);
               System.out.println(user.isActive());
           if (user != null){
                if (user.isConfirmedUser()) {
                    login(loginResponse, user);
                }
           }
        }
        return loginResponse;
    }

    private void login(LoginResponse loginResponse, User user) {
        BeanUtils.copyProperties(user, loginResponse);
        loginResponse.setMessage("Login Successful");

        user.setActive(true);
        loginResponse.setActive(user.isActive());
        userRepository.save(user);
    }

    /**
     * @param email
     * @return
     */
    @Override
    public ForgotPasswordResponse forgotPassword(String email) {
        ForgotPasswordResponse forgotPasswordResponse = new ForgotPasswordResponse();

        otpCounter++;
        OtpCreationRequest otpCreationRequest = OtpCreationRequest.builder()
                .otp(getMockOtp())
                .otpType(FORGOT_PASSWORD)
                .email(email)
                .build();

        emailSenderService.sendSimpleMail(
                EmailCreator.builder()
                        .receiverEmail(email)
                        .subject("Shield Pay Secure SignUp")

                        .body(otpService.generateOTP(otpCreationRequest))
//                                .body(otpService.generateOTP(email))
                        .build()
        );

        String message ="An OTP code has been sent to " +
                email + ". Please do not share this code with others";

        forgotPasswordResponse.setMessage(message);
        forgotPasswordResponse.setOtpSent(true);
        return forgotPasswordResponse;
    }


    private boolean isAlreadyUsedEmail(String email){
        User user = userRepository.findUserByEmail(email);
        return user != null;
    }


        private static boolean validatePassword(String password) {
            String regex = "^(?=.[0-9])(?=.[a-z])(?=.*[A-Z]).{8,}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(password);

            return matcher.matches();
        }

        private boolean validateEmail(String email) {
            Pattern p = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
            Matcher m = p.matcher(email);


            return m.matches();
        }


        /**
     * @param email
     * @return
     */
    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        return user;
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
