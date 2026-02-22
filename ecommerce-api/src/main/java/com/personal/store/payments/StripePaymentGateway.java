package com.personal.store.payments;

import com.personal.store.orders.Order;
import com.personal.store.orders.OrderItem;
import com.personal.store.orders.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StripePaymentGateway implements PaymentGateway {

    @Value("${websiteUrl}")
    private String websiteUrl;


    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    private final OrderRepository orderRepository;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {

        try {
        //Create a checkout session

        /*
        var builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                .setCancelUrl(websiteUrl + "/checkout-cancel")
                .putMetadata("order_id", order.getId().toString());
        */

        var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel")
                    .putMetadata("order_id", order.getId().toString())
                    .setPaymentIntentData(
                            SessionCreateParams.PaymentIntentData.builder()
                                    .putMetadata("order_id", order.getId().toString())
                                    .build()
                    );

        order.getItems().forEach(item -> {
            var lineItem = createLineItem(item);
            builder.addLineItem(lineItem);
        });

        var session = Session.create(builder.build());

        //

            order.setPaymentSessionId(session.getId());
            orderRepository.save(order);

        //
        return new CheckoutSession(session.getUrl());

        }catch (StripeException ex){
            System.out.println( ex.getMessage() );
            throw new PaymentException();
        }

    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {

        try {

            var payload = request.getPayload();
            var signature = request.getHeaders().get("stripe-signature");
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);

            return switch (event.getType()) {
                case "payment_intent.succeeded" ->
                        Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));

                case "payment_intent.payment_failed" ->
                        Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));

                default -> Optional.empty();

            };

        } catch (SignatureVerificationException e) {
            throw new PaymentException("Invalid signature");
        }
    }


    private Long extractOrderId(Event event) {
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
                () -> new PaymentException("Could not deserialize stripe event. Check the SDK and API version")
        );
        var paymentIntent = (PaymentIntent) stripeObject;

        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));


    }


    private SessionCreateParams.LineItem createLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(createPriceData(item))
                .build();
    }


    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("eur")
                .setUnitAmountDecimal(
                        item.getUnitPrice().multiply(BigDecimal.valueOf(100))) // cents else exception
                .setProductData(createProductData(item)).
                build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName())
                .build();
    }

    public PaymentStatus verifyPayment(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);

            if (session.getPaymentStatus() == null) {
                return PaymentStatus.PENDING;
            }

            switch (session.getPaymentStatus()) {
                case "paid":
                case "no_payment_required":
                    return PaymentStatus.PAID;
                case "unpaid":
                    return PaymentStatus.PENDING;
                case "expired":
                    return PaymentStatus.CANCELED;
                default:
                    return PaymentStatus.FAILED;
            }

        } catch (StripeException e) {
            // Could retry later or log
            throw new RuntimeException("Stripe verification failed for session " + sessionId, e);
        }
    }
}
