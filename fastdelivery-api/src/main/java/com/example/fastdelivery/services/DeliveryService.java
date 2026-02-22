package com.example.fastdelivery.services;

import com.example.fastdelivery.dtos.DeliveryRequest;
import com.example.fastdelivery.dtos.GeoCoordinates;
import com.example.fastdelivery.entities.*;
import com.example.fastdelivery.exceptions.DeliveryDoesNotExistException;
import com.example.fastdelivery.exceptions.DuplicateDeliveryException;
import com.example.fastdelivery.mappers.DeliveryMapper;
import com.example.fastdelivery.repositories.DeliveryRepository;
import com.example.fastdelivery.repositories.PendingDeliveryRepository;
import com.example.fastdelivery.clients.NominatimClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final PendingDeliveryRepository pendingRepository;
    private final DeliveryMapper mapper;
    private final NominatimClient nominatimService;

    private static final double DEPOT_LON = 4.3517;
    private static final double DEPOT_LAT = 50.8503;


    public Delivery createDelivery(DeliveryRequest request) {

        if (deliveryRepository.existsById(request.getOrderId())) {
            throw new DuplicateDeliveryException();
        }

        Delivery delivery = mapper.toEntity(request);

        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setCreatedAt(Instant.now());
        delivery.setUpdatedAt(Instant.now());


        if (delivery.getScheduledDeliveryTime() == null) {
            delivery.setScheduledDeliveryTime(
                    LocalDate.now()
                            .plusDays(1)
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
            );
        }

        deliveryRepository.save(delivery);

        addToPending(delivery);

        return delivery;
    }


    public GeoCoordinates resolveDeliveryCoordinates(Delivery delivery) {


        if (delivery.getStreet() == null || delivery.getStreet().isBlank()) {
            return new GeoCoordinates(DEPOT_LON, DEPOT_LAT);
        }

        return nominatimService.geocode(
                delivery.getStreet(),
                delivery.getStreetNumber(),
                delivery.getPostalCode(),
                delivery.getCity()
        );
    }


    public void assignDelivery(Delivery delivery,
                               UUID courierId,
                               Instant plannedStart,
                               Instant plannedEnd,
                               int estimatedMinutes,
                               LocalDate assignedDate) {

        delivery.setDeliveryPersonId(courierId);
        delivery.setPlannedStartTime(plannedStart);
        delivery.setPlannedEndTime(plannedEnd);
        delivery.setEstimatedDurationMinutes(estimatedMinutes);
        delivery.setAssignedDate(assignedDate);
        delivery.setStatus(DeliveryStatus.ASSIGNED);
        delivery.setUpdatedAt(Instant.now());

        deliveryRepository.save(delivery);

        removeFromPending(delivery, assignedDate);
    }


    public void postponeDelivery(Delivery delivery) {

        LocalDate nextDay = delivery.getScheduledDeliveryTime()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .plusDays(1);

        delivery.setScheduledDeliveryTime(
                nextDay.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
        delivery.setStatus(DeliveryStatus.POSTPONED);
        delivery.setUpdatedAt(Instant.now());

        deliveryRepository.save(delivery);

        addToPending(delivery);
    }


    public void updateStatus(Long orderId, DeliveryStatus status) {

        Delivery delivery = getById(orderId);

        delivery.setStatus(status);

        if (status == DeliveryStatus.DELIVERED) {
            delivery.setActualDeliveryTime(Instant.now());
        }

        delivery.setUpdatedAt(Instant.now());
        deliveryRepository.save(delivery);
    }



    public Delivery getById(Long orderId) {
        return deliveryRepository.findById(orderId)
                .orElseThrow(() -> new DeliveryDoesNotExistException());
    }



    private void addToPending(Delivery delivery) {

        LocalDate date = delivery.getScheduledDeliveryTime()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        DeliveryPendingKey key = new DeliveryPendingKey();
        key.setOrderId(delivery.getOrderId());
        key.setScheduledDate(date);

        DeliveryPendingEntity pending = new DeliveryPendingEntity();
        pending.setKey(key);

        pendingRepository.save(pending);
    }

    private void removeFromPending(Delivery delivery, LocalDate date) {

        DeliveryPendingKey key = new DeliveryPendingKey();
        key.setOrderId(delivery.getOrderId());
        key.setScheduledDate(date);

        pendingRepository.deleteById(key);
    }
}
