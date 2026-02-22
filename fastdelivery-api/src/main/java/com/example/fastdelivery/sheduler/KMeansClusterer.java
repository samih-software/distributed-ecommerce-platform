package com.example.fastdelivery.sheduler;




import com.example.fastdelivery.dtos.GeoCoordinates;
import org.springframework.stereotype.Component;

import java.util.*;
        import java.util.stream.Collectors;

@Component
public class KMeansClusterer {

    public <T extends HasCoordinates> Map<Integer, List<T>> cluster(
            List<T> points,
            int k) {

        if (points.isEmpty()) return Map.of();

        k = Math.min(k, points.size());

        List<GeoCoordinates> centroids =
                points.stream()
                        .limit(k)
                        .map(HasCoordinates::getCoordinates)
                        .collect(Collectors.toList());

        Map<T, Integer> assignments = new HashMap<>();

        boolean changed;
        int iterations = 0;

        do {
            changed = false;
            iterations++;

            for (T point : points) {

                int nearest = 0;
                double minDist = distance(point.getCoordinates(),
                        centroids.get(0));

                for (int i = 1; i < k; i++) {
                    double d = distance(point.getCoordinates(),
                            centroids.get(i));
                    if (d < minDist) {
                        minDist = d;
                        nearest = i;
                    }
                }

                if (!Objects.equals(assignments.get(point), nearest)) {
                    assignments.put(point, nearest);
                    changed = true;
                }
            }

            for (int i = 0; i < k; i++) {

                int clusterIndex = i;

                List<T> clusterPoints =
                        assignments.entrySet().stream()
                                .filter(e -> e.getValue() == clusterIndex)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList());

                if (!clusterPoints.isEmpty()) {

                    double avgLat = clusterPoints.stream()
                            .mapToDouble(p -> p.getCoordinates().getLat())
                            .average().orElse(0);

                    double avgLon = clusterPoints.stream()
                            .mapToDouble(p -> p.getCoordinates().getLon())
                            .average().orElse(0);

                    centroids.set(i, new GeoCoordinates(avgLon, avgLat));
                }
            }

        } while (changed && iterations < 10);

        return assignments.entrySet().stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey,
                                Collectors.toList())
                ));
    }

    private double distance(GeoCoordinates a, GeoCoordinates b) {

        final int R = 6371000;

        double latRad1 = Math.toRadians(a.getLat());
        double latRad2 = Math.toRadians(b.getLat());
        double deltaLat = Math.toRadians(b.getLat() - a.getLat());
        double deltaLon = Math.toRadians(b.getLon() - a.getLon());

        double h = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(latRad1) * Math.cos(latRad2)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        return 2 * R * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h));
    }

    public interface HasCoordinates {
        GeoCoordinates getCoordinates();
    }
}