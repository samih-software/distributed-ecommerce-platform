package com.personal.store.products;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph("category")
    List<Product> findByCategoryId(Byte CategoryId);

    @EntityGraph("category")
    @Query("SELECT p FROM Product p")
    List<Product> findAllWithCategory();
}