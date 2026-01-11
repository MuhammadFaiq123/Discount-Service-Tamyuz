package com.assignment.tamyuz.discount.service.app.controller;

import com.assignment.tamyuz.discount.service.domain.order.model.OrderRequest;
import com.assignment.tamyuz.discount.service.domain.order.model.OrderResponse;
import com.assignment.tamyuz.discount.service.domain.order.service.OrderService;
import com.assignment.tamyuz.discount.service.utils.PagedResponse;
import com.assignment.tamyuz.discount.service.utils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private JwtAuthenticationToken auth;

    @BeforeEach
    void setUp() {
        auth = new JwtAuthenticationToken(mock(Jwt.class), null, "email");
    }

    @Test
    void testPlaceOrder() {
        OrderRequest request = TestHelper.createDefaultOrderRequest();
        OrderResponse response = TestHelper.createDefaultOrderResponse();
        when(orderService.placeOrder(eq(auth.getName()), anyList()))
                .thenReturn(response);

        OrderResponse result = orderController.placeOrder(auth, request);

        assertEquals(response, result);
        verify(orderService).placeOrder(eq(auth.getName()), anyList());
    }

    @Test
    void testMyOrders() {
        PagedResponse<OrderResponse> pagedResponse = new PagedResponse<>(List.of(), 0, 10, 0, 10);
        when(orderService.getOrdersByUser(auth.getName(), 0, 10, "createdAt", Sort.Direction.DESC))
                .thenReturn(pagedResponse);

        PagedResponse<OrderResponse> result = orderController.myOrders(auth, 0, 10, "createdAt", Sort.Direction.DESC);

        assertEquals(pagedResponse, result);
        verify(orderService).getOrdersByUser(auth.getName(), 0, 10, "createdAt", Sort.Direction.DESC);
    }
}

