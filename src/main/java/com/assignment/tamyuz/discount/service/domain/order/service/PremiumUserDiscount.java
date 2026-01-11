package com.assignment.tamyuz.discount.service.domain.order.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PremiumUserDiscount implements DiscountStrategy {

    @Override
    public BigDecimal applyDiscount(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.10));
    }
}