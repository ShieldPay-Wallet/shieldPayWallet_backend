package com.wallet.shieldpay.exceoptions;

public class InValidEmailException extends RuntimeException {
    public InValidEmailException(String message){
        super(message);
    }
}
