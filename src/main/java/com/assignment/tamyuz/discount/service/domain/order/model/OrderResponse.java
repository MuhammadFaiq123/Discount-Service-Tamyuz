package com.assignment.tamyuz.discount.service.domain.order.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private Long orderId;
    private BigDecimal orderTotal;
    private List<OrderItemResponse> items;
    private Timestamp createdAt;
}
