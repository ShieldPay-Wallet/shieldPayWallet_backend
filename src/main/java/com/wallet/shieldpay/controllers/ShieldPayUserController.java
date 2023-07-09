package com.wallet.shieldpay.controllers;

import com.wallet.shieldpay.dto.requests.SignUpRequest;
import com.wallet.shieldpay.dto.response.SignUpResponse;
import com.wallet.shieldpay.services.serviceInterface.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class ShieldPayUserController {

    @Autowired
    UserService userService ;
    public void register(@Valid String string){

    }
    @PostMapping("/signUp")
    public SignUpResponse signUp(SignUpRequest signUpRequest){
        return  userService.signUp(signUpRequest);
    }
}
