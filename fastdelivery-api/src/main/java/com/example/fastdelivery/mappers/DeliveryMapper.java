package com.example.fastdelivery.mappers;

import com.example.fastdelivery.dtos.DeliveryRequest;
import com.example.fastdelivery.entities.Delivery;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    Delivery toEntity(DeliveryRequest request);

}
