package com.wallet.shieldpay.models.UtilityModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailCreator {
    private String senderEmail = "osisiogubenjamin1@gmail.com";
    private String receiverEmail;
    private String subject;
    private String body;
}
