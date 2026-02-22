package com.example.fastdelivery.sheduler;



import com.example.fastdelivery.clients.OsrmClient;
import com.example.fastdelivery.dtos.GeoCoordinates;
import com.example.fastdelivery.entities.*;
import com.example.fastdelivery.services.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryAssignmentService {

    private final DeliveryService deliveryService;
    private final OsrmClient osrmClient;
    private final KMeansClusterer clusterer;

    private static final double DEPOT_LON = 4.3517;
    private static final double DEPOT_LAT = 50.8503;

    private final List<UUID> couriers = List.of(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            UUID.fromString("22222222-2222-2222-2222-222222222222")
    );

    public void assign(List<DeliveryPendingEntity> pending, LocalDate targetDate) {

        if (pending.isEmpty()) return;

        Instant workStart = targetDate.atTime(8, 0)
                .atZone(ZoneId.systemDefault()).toInstant();
        Instant workEnd = targetDate.atTime(18, 0)
                .atZone(ZoneId.systemDefault()).toInstant();

        List<Candidate> candidates = convertToCandidates(pending);
        Map<UUID, CourierState> courierStates = initializeCouriers(workStart);

        List<Candidate> unassigned =
                greedyAssign(candidates, courierStates, workEnd, targetDate);

        if (!unassigned.isEmpty() && couriers.size() > 1) {
            assignUsingClusters(unassigned, courierStates, workEnd, targetDate);
        }
    }

    private List<Candidate> convertToCandidates(List<DeliveryPendingEntity> pending) {
        return pending.stream()
                .map(pe -> {
                    Delivery delivery =
                            deliveryService.getById(pe.getKey().getOrderId());
                    GeoCoordinates coords =
                            deliveryService.resolveDeliveryCoordinates(delivery);
                    return new Candidate(delivery, coords);
                })
                .collect(Collectors.toList());
    }

    private Map<UUID, CourierState> initializeCouriers(Instant start) {
        Map<UUID, CourierState> states = new HashMap<>();
        for (UUID courier : couriers) {
            states.put(courier,
                    new CourierState(start,
                            new GeoCoordinates(DEPOT_LON, DEPOT_LAT)));
        }
        return states;
    }

    private List<Candidate> greedyAssign(
            List<Candidate> candidates,
            Map<UUID, CourierState> states,
            Instant workEnd,
            LocalDate date) {

        List<Candidate> unassigned = new ArrayList<>();

        for (Candidate candidate : candidates) {

            boolean assigned = false;

            for (UUID courierId : couriers) {

                CourierState state = states.get(courierId);

                long travelSeconds = osrmClient.estimateSeconds(
                        state.getPosition().getLon(),
                        state.getPosition().getLat(),
                        candidate.coords.getLon(),
                        candidate.coords.getLat()
                );

                Instant finish = state.getCurrentTime()
                        .plusSeconds(travelSeconds);

                if (finish.isBefore(workEnd)) {

                    deliveryService.assignDelivery(
                            candidate.delivery,
                            courierId,
                            state.getCurrentTime(),
                            finish,
                            (int) (travelSeconds / 60),
                            date
                    );

                    state.update(finish, candidate.coords);
                    assigned = true;
                    break;
                }
            }

            if (!assigned) unassigned.add(candidate);
        }

        return unassigned;
    }

    private void assignUsingClusters(
            List<Candidate> unassigned,
            Map<UUID, CourierState> states,
            Instant workEnd,
            LocalDate date) {

        Map<Integer, List<Candidate>> clusters =
                clusterer.cluster(unassigned, couriers.size());

        int index = 0;

        for (UUID courierId : couriers) {

            List<Candidate> cluster =
                    clusters.getOrDefault(index, List.of());

            CourierState state = states.get(courierId);

            for (Candidate candidate : cluster) {

                long travelSeconds = osrmClient.estimateSeconds(
                        state.getPosition().getLon(),
                        state.getPosition().getLat(),
                        candidate.coords.getLon(),
                        candidate.coords.getLat()
                );

                Instant finish =
                        state.getCurrentTime().plusSeconds(travelSeconds);

                if (finish.isBefore(workEnd)) {

                    deliveryService.assignDelivery(
                            candidate.delivery,
                            courierId,
                            state.getCurrentTime(),
                            finish,
                            (int) (travelSeconds / 60),
                            date
                    );

                    state.update(finish, candidate.coords);
                } else {
                    deliveryService.postponeDelivery(candidate.delivery);
                }
            }

            index++;
        }
    }




    private static class Candidate implements KMeansClusterer.HasCoordinates {
        final Delivery delivery;
        final GeoCoordinates coords;

        Candidate(Delivery delivery, GeoCoordinates coords) {
            this.delivery = delivery;
            this.coords = coords;
        }

        @Override
        public GeoCoordinates getCoordinates() {
            return coords;
        }
    }


}