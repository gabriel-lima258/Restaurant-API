package com.gtech.food_api.domain.service.exceptions;

public class PaymentNoAcceptedException extends BusinessException {
    public PaymentNoAcceptedException(String message) {
        super(message);
    }

    public PaymentNoAcceptedException(String paymentMethodDescription, String restaurantName) {
        this(String.format("Payment method %s is not accepted by restaurant %s", paymentMethodDescription, restaurantName));
    }
}
