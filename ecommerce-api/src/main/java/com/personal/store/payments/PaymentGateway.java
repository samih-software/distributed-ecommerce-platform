package com.personal.store.payments;

import com.personal.store.orders.Order;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);

    PaymentStatus verifyPayment(String externalPaymentId);
}
