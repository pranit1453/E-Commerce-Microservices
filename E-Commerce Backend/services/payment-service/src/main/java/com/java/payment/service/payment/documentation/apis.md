1. Order Create and Payment Create
   POST : http://localhost:8084/createPaymentAsOrderCreated
   Request Body:
   {
   "orderId" : "",
   "amount" : .00,
   "userId" : ""
   }

   Response Body:
   paymentId

2. Initiate Payment
   POST : http://localhost:8084/api/v1/payment/initiate
   Request Body:
   {
   "paymentId":"",
   "gateway":"DUMMY",
   "service":"UPI",
   "detail":{
   "type":"UPI",
   "upiId":"abc@okicici"
   }
   }

   Response Body:
   {
   "paymentId": "",
   "gatewayOrderId": ""
   }

3. Verify Payment
   POST : http://localhost:8084/api/v1/payment/verify
   Request Body:
   {
   "paymentId": "",
   "gatewayOrderId": "",
   "gatewayPaymentId": "",
   "gatewaySignature":  ""
   }

   Response Body:
   Payment Verified OR Payment Failed