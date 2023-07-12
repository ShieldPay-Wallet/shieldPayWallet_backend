package com.wallet.shieldpay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    private boolean isSuccessFul;
    private Object data;
}
