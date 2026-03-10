package com.personal.store.payments;

import com.personal.store.orders.Order;
import com.personal.store.orders.OrderRepository;
import com.personal.store.payments.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentRecoveryService {

    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;



    /**
     * Scheduled job to reconcile recent PENDING orders with Stripe.
     * Runs every 10 minutes.
     */
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void reconcilePendingPayments() {
        int page = 0;
        int batchSize = 50; // safe batch size to avoid rate limits

        List<Order> batch;
        do {
            batch = orderRepository.findPendingOrdersCreatedAfter(
                    LocalDateTime.now().minusHours(24),
                    PageRequest.of(page, batchSize),
                    PaymentStatus.PENDING
            );

            for (Order order : batch) {
                reconcileSingleOrder(order);
            }

            page++;
        } while (!batch.isEmpty());
    }

    
    /**
     * Core logic to reconcile a single order.
     * Checks Stripe payment status and updates order if needed.
     */
    private void reconcileSingleOrder(Order order) {
        if (order.getStatus() == PaymentStatus.PAID) return; // already paid
        if (order.getPaymentSessionId() == null) return; // nothing to verify

        try {
            PaymentStatus status = paymentGateway.verifyPayment(order.getPaymentSessionId());
            if (status == PaymentStatus.PAID) {
                order.setStatus(PaymentStatus.PAID);
            }
        } catch (Exception e) {
            // log error and continue with other orders
            System.err.println("Payment reconciliation failed for order " + order.getId());
            e.printStackTrace();
        }
    }


}
