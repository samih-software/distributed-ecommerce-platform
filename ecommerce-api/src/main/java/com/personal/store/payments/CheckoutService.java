package com.personal.store.payments;


import com.personal.store.orders.Order;
import com.personal.store.carts.CartEmptyException;
import com.personal.store.carts.CartNotFoundException;
import com.personal.store.carts.CartRepository;
import com.personal.store.orders.OrderRepository;
import com.personal.store.auth.AuthService;
import com.personal.store.carts.CartService;
import com.personal.store.users.Address;
import com.personal.store.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@RequiredArgsConstructor // only final fields as constructor
@Service
public class CheckoutService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    private final CartService cartService;
    private final AuthService authService;

    private final PaymentGateway paymentGateway;


    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {

        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        if(cart.isEmpty()){
            throw new CartEmptyException();
        }



        var order = Order.fromCart(cart,authService.getCurrentUser());

        orderRepository.save(order);

        try{

            var session = paymentGateway.createCheckoutSession(order);

            cartService.getCart(cart.getId());

            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());

        }catch(PaymentException e){
            orderRepository.delete(order);
            throw e;
        }

    }


    public void handleWebhookEvent(WebhookRequest request) {

        paymentGateway.parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);


                });

    }





}
