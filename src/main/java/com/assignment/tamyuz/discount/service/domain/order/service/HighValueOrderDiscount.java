package com.assignment.tamyuz.discount.service.domain.order.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HighValueOrderDiscount implements DiscountStrategy {

    @Override
    public BigDecimal applyDiscount(BigDecimal amount) {
        return amount.compareTo(BigDecimal.valueOf(500)) > 0
                ? amount.multiply(BigDecimal.valueOf(0.05))
                : BigDecimal.ZERO;
    }
}