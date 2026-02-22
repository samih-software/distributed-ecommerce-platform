package com.personal.store.payments;


import com.personal.store.common.ErrorDto;
import com.personal.store.carts.CartEmptyException;
import com.personal.store.carts.CartNotFoundException;
import com.personal.store.orders.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {


    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;


    @PostMapping
    public CheckoutResponse checkout(
            @Valid @RequestBody CheckoutRequest request) {

            return checkoutService.checkout(request);

    }

    @PostMapping("/webhook")
    public void handleWebhook(
            @RequestHeader Map<String, String> signature,
            @RequestBody String payload
    ){
        checkoutService.handleWebhookEvent(new WebhookRequest(signature, payload));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body(new ErrorDto("Error creating a checkout session"));
    }


    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception exception) {

        return ResponseEntity.badRequest().body(new ErrorDto(exception.getMessage()));


    }

}
