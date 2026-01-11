package com.assignment.tamyuz.discount.service.domain.order.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;
}

