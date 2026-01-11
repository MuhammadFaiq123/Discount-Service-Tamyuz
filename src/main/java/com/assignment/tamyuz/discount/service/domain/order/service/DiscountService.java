package com.assignment.tamyuz.discount.service.domain.order.service;

import com.assignment.tamyuz.discount.service.persistence.entity.Role;
import com.assignment.tamyuz.discount.service.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final PremiumUserDiscount premiumUserDiscount;
    private final HighValueOrderDiscount highValueOrderDiscount;

    public BigDecimal calculateTotalDiscount(User user, BigDecimal orderTotal) {

        BigDecimal discount = BigDecimal.ZERO;

        // User-based discount
        if (user.getRole().equals(Role.ROLE_PREMIUM_USER)) {
            discount = discount.add(premiumUserDiscount.applyDiscount(orderTotal));
        }

        // High value order discount
        discount = discount.add(highValueOrderDiscount.applyDiscount(orderTotal));

        return discount;
    }
}