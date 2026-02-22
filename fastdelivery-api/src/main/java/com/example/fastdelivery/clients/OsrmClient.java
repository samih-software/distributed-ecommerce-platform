package com.example.fastdelivery.clients;


import com.example.fastdelivery.dtos.OsrmResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OsrmClient {


    @Value("${osrm.base-url}")
    private String osrmBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public long estimateSeconds(double fromLon, double fromLat,
                                double toLon, double toLat) {

        String url = String.format(
                "%s/%f,%f;%f,%f?overview=false",
                osrmBaseUrl,
                fromLon, fromLat, toLon, toLat
        );

        OsrmResponse response =
                restTemplate.getForObject(url, OsrmResponse.class);

        double durationSeconds =
                response.getRoutes().get(0).getDuration();

        return Math.round(durationSeconds);
    }
}