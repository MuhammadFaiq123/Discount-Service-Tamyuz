package com.assignment.tamyuz.discount.service.app.controller;

import com.assignment.tamyuz.discount.service.domain.order.model.OrderRequest;
import com.assignment.tamyuz.discount.service.domain.order.model.OrderResponse;
import com.assignment.tamyuz.discount.service.domain.order.service.OrderService;
import com.assignment.tamyuz.discount.service.utils.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management APIs")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Place an order")
    @ApiResponse(responseCode = "200", description = "Order placed successfully")
    @PostMapping
    public OrderResponse placeOrder(
            JwtAuthenticationToken authentication,
            @RequestBody @Valid OrderRequest request
    ) {
        return orderService.placeOrder(authentication.getName(), request.getItems());
    }

    @Operation(summary = "Get my orders (paginated)")
    @ApiResponse(responseCode = "200", description = "Orders fetched successfully")
    @GetMapping
    public PagedResponse<OrderResponse> myOrders(
            JwtAuthenticationToken authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        return orderService.getOrdersByUser(authentication.getName(), page, size, sortBy, direction);
    }

}