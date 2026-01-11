package com.assignment.tamyuz.discount.service.domain.order.mapper;

import com.assignment.tamyuz.discount.service.domain.order.model.OrderItemResponse;
import com.assignment.tamyuz.discount.service.domain.order.model.OrderResponse;
import com.assignment.tamyuz.discount.service.persistence.entity.Order;
import com.assignment.tamyuz.discount.service.persistence.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", source = "id")
    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponse(List<Order> orders);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    OrderItemResponse toItemResponse(OrderItem item);
}

