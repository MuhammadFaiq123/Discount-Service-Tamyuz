package com.assignment.tamyuz.discount.service.utils;

import com.assignment.tamyuz.discount.service.domain.order.model.OrderItemRequest;
import com.assignment.tamyuz.discount.service.domain.order.model.OrderItemResponse;
import com.assignment.tamyuz.discount.service.domain.order.model.OrderRequest;
import com.assignment.tamyuz.discount.service.domain.order.model.OrderResponse;
import com.assignment.tamyuz.discount.service.domain.product.model.ProductRequest;
import com.assignment.tamyuz.discount.service.domain.product.model.ProductResponse;
import com.assignment.tamyuz.discount.service.persistence.entity.Product;
import com.assignment.tamyuz.discount.service.persistence.entity.Role;
import com.assignment.tamyuz.discount.service.persistence.entity.User;

import java.math.BigDecimal;
import java.util.List;

public class TestHelper {

    public static Product createDefaultProduct() {
        return Product.builder()
                .id(1L)
                .name("Default Name")
                .price(BigDecimal.ONE)
                .quantity(200)
                .deleted(false)
                .build();
    }

    public static User createDefaultUserWithUserRole() {
        return User.builder()
                .id(1L)
                .email("userTemp@example.com")
                .password("password123")
                .role(Role.ROLE_USER)
                .build();
    }

    public static OrderResponse createDefaultOrderResponse(){
        return OrderResponse.builder()
                .orderId(1L)
                .orderTotal(BigDecimal.TEN)
                .items(List.of(createDefaultOrderItemResponse()))
                .build();
    }

    public static OrderItemResponse createDefaultOrderItemResponse(){
        return OrderItemResponse.builder()
                .productId(1L)
                .productName("Default Name")
                .quantity(10)
                .unitPrice(BigDecimal.TEN)
                .discountApplied(BigDecimal.ONE)
                .totalPrice(BigDecimal.TEN)
                .build();
    }

    public static OrderItemRequest createDefaultOrderItemRequest(){
        return new OrderItemRequest(1L, 2);
    }

    public static OrderRequest createDefaultOrderRequest(){
        return new OrderRequest(List.of(createDefaultOrderItemRequest()));
    }

    public static ProductResponse createDefaultProductResponse() {
        return ProductResponse.builder()
                .id(1L)
                .name("Default Name")
                .description("Default Description")
                .price(BigDecimal.TEN)
                .quantity(10)
                .build();

    }

    public static ProductRequest createDefaultProductRequest() {
        return ProductRequest.builder()
                .name("Default Name")
                .description("Default Description")
                .price(BigDecimal.TEN)
                .quantity(10)
                .build();
    }
}
