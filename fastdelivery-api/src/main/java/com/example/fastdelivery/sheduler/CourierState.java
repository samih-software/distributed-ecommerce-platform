package com.example.fastdelivery.sheduler;


import com.example.fastdelivery.dtos.GeoCoordinates;
import lombok.Getter;

import java.time.Instant;


@Getter
public class CourierState {

    private Instant currentTime;
    private GeoCoordinates position;

    public CourierState(Instant currentTime, GeoCoordinates position) {
        this.currentTime = currentTime;
        this.position = position;
    }

    public void update(Instant newTime, GeoCoordinates newPosition) {
        this.currentTime = newTime;
        this.position = newPosition;
    }
}