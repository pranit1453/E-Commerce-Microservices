package com.java.payment.service.details;

import lombok.Data;

@Data
public class UpiDetails implements PaymentDetails {
    private String upiId;
}
