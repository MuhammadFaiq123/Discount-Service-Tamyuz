package com.assignment.tamyuz.discount.service.persistence.repository;

import com.assignment.tamyuz.discount.service.persistence.entity.Order;
import com.assignment.tamyuz.discount.service.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUser(User user, Pageable pageable);
}