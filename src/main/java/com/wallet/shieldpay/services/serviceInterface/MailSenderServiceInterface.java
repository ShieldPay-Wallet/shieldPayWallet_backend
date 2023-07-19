package com.wallet.shieldpay.services.serviceInterface;

import com.wallet.shieldpay.models.UtilityModels.EmailCreator;

public interface MailSenderServiceInterface {
     void sendSimpleMail(EmailCreator emailCreator);
}
