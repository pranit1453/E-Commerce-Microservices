package com.java.payment.service.details;

import lombok.Data;

@Data
public class NetBankingDetails implements PaymentDetails {
    private String bankCode;
    private String accountNumber;
}
