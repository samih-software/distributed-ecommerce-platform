package com.personal.store.delivery;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
@RequestMapping("/deliveries")
public class DeliveryForwardController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String deliveryUrl = "http://localhost:8181/deliveries";

    @PostMapping
    public ResponseEntity<byte[]> forwardPost(
            @RequestHeader HttpHeaders headers,
            @RequestBody(required = false) byte[] body
    ) {
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(
                body == null ? new byte[0] : body,
                headers
        );

        return restTemplate.exchange(
                deliveryUrl,
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<byte[]> forwardGet(
            @PathVariable String orderId,
            @RequestHeader HttpHeaders headers
    ) {
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                deliveryUrl + "/" + orderId,
                HttpMethod.GET,
                requestEntity,
                byte[].class
        );
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<byte[]> forwardPatch(
            @PathVariable String orderId,
            @RequestHeader HttpHeaders headers,
            @RequestBody(required = false) byte[] body
    ) {
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(
                body == null ? new byte[0] : body,
                headers
        );

        return restTemplate.exchange(
                deliveryUrl + "/" + orderId,
                HttpMethod.PATCH,
                requestEntity,
                byte[].class
        );
    }
}
