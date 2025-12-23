package com.gtech.food_api.domain.service.exceptions;

public class PaymentNoAcceptedException extends BusinessException {
    public PaymentNoAcceptedException(String message) {
        super(message);
    }

    public PaymentNoAcceptedException(String paymentMethodDescription, String restaurantName) {
        this(String.format("Método de pagamento %s não é aceito pelo restaurante %s", paymentMethodDescription, restaurantName));
    }
}
