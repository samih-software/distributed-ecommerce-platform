package com.personal.store.orders;

import com.personal.store.payments.PaymentStatus;
import com.personal.store.users.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

public interface OrderRepository extends JpaRepository<Order, Long> {



    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT o FROM Order o WHERE o.customer = :customer")
    List<Order> getOrdersByCustomer(@Param("customer") User customer);

    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT o FROM Order o WHERE o.id = :orderId")
    Optional<Order> getOrderWithItems(@Param("orderId") Long orderId);


    @Query("SELECT o FROM Order o " +
            "WHERE o.status = :status " +
            "AND o.paymentSessionId IS NOT NULL " +
            "AND o.createdAt >= :after")
    List<Order> findPendingOrdersCreatedAfter(
            @Param("after") LocalDateTime after,
            Pageable pageable,
            @Param("status") PaymentStatus status
    );
}