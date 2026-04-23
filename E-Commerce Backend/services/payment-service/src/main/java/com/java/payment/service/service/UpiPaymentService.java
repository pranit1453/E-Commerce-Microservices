package com.java.payment.service.service;

import com.java.payment.service.custom.GatewayType;
import com.java.payment.service.dto.PaymentGatewayData;
import com.java.payment.service.dto.PaymentGatewayResponse;
import com.java.payment.service.util.HmacUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@GatewayType("DUMMY")
public class UpiPaymentService implements PaymentMethodService {


    @Override
    public String getServiceType() {
        return "UPI";
    }


    @Override
    public PaymentGatewayResponse initiate(PaymentGatewayData data) {

        // verify upi id and if verified then generate the gid and g_pid and signature
        String gatewayOrderId = GenerateIDs.generateGatewayOrderId(data.orderId());
        String gatewayPaymentId = GenerateIDs.generateGatewayPaymentId(data.paymentId());

        String dataToSign = gatewayOrderId + "_upi_" + data.amount() + gatewayPaymentId;
        String gatewaySignature = HmacUtil.generateSignature(dataToSign);
        return PaymentGatewayResponse.builder()
                .gatewayOrderId(gatewayOrderId)
                .gatewayPaymentId(gatewayPaymentId)
                .gatewaySignature(gatewaySignature)
                .build();
    }
}
