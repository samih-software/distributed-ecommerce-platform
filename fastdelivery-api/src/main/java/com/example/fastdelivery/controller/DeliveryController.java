package com.example.fastdelivery.controller;

import com.example.fastdelivery.dtos.DeliveryRequest;
import com.example.fastdelivery.dtos.ErrorDto;
import com.example.fastdelivery.entities.Delivery;
import com.example.fastdelivery.exceptions.DeliveryDoesNotExistException;
import com.example.fastdelivery.exceptions.DuplicateDeliveryException;
import com.example.fastdelivery.services.DeliveryService;
import com.example.fastdelivery.entities.DeliveryStatus;
import com.example.fastdelivery.repositories.DeliveryRepository;
import com.example.fastdelivery.sheduler.NightlyAssignmentJob;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryRepository repository;

    private final DeliveryService deliveryService;


    @PostMapping
    public void createDelivery(@RequestBody DeliveryRequest request) {
        System.out.println(request.toString());
        deliveryService.createDelivery(request);
    }


    @GetMapping("/{orderId}")
    public Delivery get(@PathVariable Long orderId) {
        return deliveryService.getById(orderId);
    }


    @PatchMapping("/{orderId}")
    public void updateStatus(
            @PathVariable Long orderId,
            @RequestParam DeliveryStatus status
    ) {
        deliveryService.updateStatus(orderId, status);
    }


    private final NightlyAssignmentJob job;

    @PostMapping("/run-assignment")
    public void runAssignment() {
        job.assignDeliveries();
    }



    @ExceptionHandler(DeliveryDoesNotExistException.class)
    public ResponseEntity<ErrorDto> handleDeliveryNotFound(DeliveryDoesNotExistException ex) {
        ErrorDto error = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(DuplicateDeliveryException.class)
    public ResponseEntity<ErrorDto> handleDuplicateDelivery(DuplicateDeliveryException ex) {
        ErrorDto error = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
