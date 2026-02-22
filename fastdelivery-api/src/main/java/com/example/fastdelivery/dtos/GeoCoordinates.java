package com.example.fastdelivery.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeoCoordinates {
    private final double lon;
    private final double lat;
}