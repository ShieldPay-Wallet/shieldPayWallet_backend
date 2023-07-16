package com.wallet.shieldpay.exceptions;

public class InValidEmailException extends RuntimeException {
    public InValidEmailException(String message){
        super(message);
    }
}
