package com.assignment.tamyuz.discount.service.domain.order.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountApplied;
    private BigDecimal totalPrice;
}

