package com.wallet.shieldpay.controllers;

import com.wallet.shieldpay.dto.requests.ChangePasswordRequest;
import com.wallet.shieldpay.dto.requests.SignUpRequest;
import com.wallet.shieldpay.dto.response.ApiResponse;
import com.wallet.shieldpay.services.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ewallet/user")

public class ShieldPayUserController {

    @Autowired
    UserService userService ;
//    public void register(@Valid String string){
//
//    }
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest){
        System.out.println(signUpRequest );
       return new ResponseEntity<>(new ApiResponse(true ,userService.signUp(signUpRequest))
                , HttpStatus.CREATED);
    }
    @PostMapping("/otpConfirmation/{otp}")
    public ResponseEntity<?> userOTPCodeConfirmation(@PathVariable String otp){

        return new ResponseEntity<>(new ApiResponse(true, userService.userOTPCodeConfirmation(otp))
                , HttpStatus.ACCEPTED);
    }
    @GetMapping("/userLogin/{email}/{password}")
    public ResponseEntity<?> login(@PathVariable String email,@PathVariable String password){
        return new ResponseEntity<>(new ApiResponse(true, userService.login(email, password))
                ,HttpStatus.ACCEPTED);
    }
    @PatchMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        return new ResponseEntity<>(
                new ApiResponse(true,
                        userService.changePassword(changePasswordRequest)
                )
                ,HttpStatus.ACCEPTED);
    }


}
