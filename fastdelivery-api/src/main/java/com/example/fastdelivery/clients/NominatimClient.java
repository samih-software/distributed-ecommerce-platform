package com.example.fastdelivery.clients;

import com.example.fastdelivery.dtos.GeoCoordinates;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NominatimClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${nominatim.search-url}")
    private String nominatimSearchUrl;

    public GeoCoordinates geocode(
            String street,
            String streetNumber,
            String postalCode,
            String city
    ) {
        String query = String.format(
                "%s %s, %s %s",
                street, streetNumber, postalCode, city
        );

        String encodedQuery = query.replace(" ", "+");

        String url = String.format(
                "%s?q=%s&format=json&limit=1",
                nominatimSearchUrl,
                encodedQuery
        );

        System.out.println("URL: " + url);

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            if (!root.isArray() || root.isEmpty()) {
                throw new IllegalStateException("Address not found: " + query);
            }

            JsonNode node = root.get(0);
            double lat = node.get("lat").asDouble();
            double lon = node.get("lon").asDouble();

            return new GeoCoordinates(lon, lat);

        } catch (Exception e) {
            throw new RuntimeException("Failed to geocode address: " + query, e);
        }
    }
}
