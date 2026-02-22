package com.personal.store.orders;

import com.personal.store.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {

    private  final OrderRepository orderRepository;
    private final AuthService authService;
    private  final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders()
    {

        var user = authService.getCurrentUser();

        var orders = orderRepository.getOrdersByCustomer(user);


        return orders.stream()
                .map(orderMapper::toDto)
                .toList();


    }

    public OrderDto getOrder(Long orderId)
    {


        var order = orderRepository
                .getOrderWithItems(orderId)
                .orElseThrow(OrderNotFoundException::new);


        var user = authService.getCurrentUser();

        if( ! order.isPlacedBy(user)){
            throw new AccessDeniedException(
                    "You don't have access this order"
            );
        }

        return orderMapper.toDto(order);

    }

    public List<OrderDto> getCurrentUserOrders()
    {


        var orders = orderRepository
                .getOrdersByCustomer(authService.getCurrentUser());

        var orderDtos = orders.stream()
                .map(orderMapper::toDto)
                .toList();

        return orderDtos;

    }




}
