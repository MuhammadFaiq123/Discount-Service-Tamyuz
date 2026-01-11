package com.assignment.tamyuz.discount.service.domain.order.service;

import com.assignment.tamyuz.discount.service.persistence.entity.Role;
import com.assignment.tamyuz.discount.service.persistence.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

    @Mock
    private PremiumUserDiscount premiumUserDiscount;

    @Mock
    private HighValueOrderDiscount highValueOrderDiscount;

    @InjectMocks
    private DiscountService discountService;

    @Test
    void calculateTotalDiscount_premiumUser_appliesBothDiscounts() {
        User user = new User();
        user.setRole(Role.ROLE_PREMIUM_USER);

        BigDecimal orderTotal = BigDecimal.valueOf(100);

        when(premiumUserDiscount.applyDiscount(orderTotal))
                .thenReturn(BigDecimal.TEN);
        when(highValueOrderDiscount.applyDiscount(orderTotal))
                .thenReturn(BigDecimal.valueOf(5));

        BigDecimal result = discountService.calculateTotalDiscount(user, orderTotal);
        assertEquals(BigDecimal.valueOf(15), result);
    }

    @Test
    void calculateTotalDiscount_regularUser_appliesHighValueOnly() {
        User user = new User();
        user.setRole(Role.ROLE_USER);

        BigDecimal orderTotal = BigDecimal.valueOf(100);

        when(highValueOrderDiscount.applyDiscount(orderTotal))
                .thenReturn(BigDecimal.valueOf(8));

        BigDecimal result = discountService.calculateTotalDiscount(user, orderTotal);
        assertEquals(BigDecimal.valueOf(8), result);
    }
}

