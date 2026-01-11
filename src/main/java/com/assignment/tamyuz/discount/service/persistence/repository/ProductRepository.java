package com.assignment.tamyuz.discount.service.persistence.repository;

import com.assignment.tamyuz.discount.service.persistence.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.deleted = true WHERE p.id = :id")
    void softDelete(@Param("id") Long id);
}

