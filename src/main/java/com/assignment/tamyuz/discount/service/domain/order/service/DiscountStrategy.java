package com.assignment.tamyuz.discount.service.domain.order.service;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal applyDiscount(BigDecimal amount);
}