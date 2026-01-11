package com.assignment.tamyuz.discount.service.domain.order.service;

import com.assignment.tamyuz.discount.service.app.exception.BadRequestException;
import com.assignment.tamyuz.discount.service.app.exception.NotFoundException;
import com.assignment.tamyuz.discount.service.domain.order.mapper.OrderMapper;
import com.assignment.tamyuz.discount.service.domain.order.model.OrderItemRequest;
import com.assignment.tamyuz.discount.service.domain.order.model.OrderRequest;
import com.assignment.tamyuz.discount.service.domain.order.model.OrderResponse;
import com.assignment.tamyuz.discount.service.persistence.entity.Order;
import com.assignment.tamyuz.discount.service.persistence.entity.Product;
import com.assignment.tamyuz.discount.service.persistence.entity.User;
import com.assignment.tamyuz.discount.service.persistence.repository.OrderRepository;
import com.assignment.tamyuz.discount.service.persistence.repository.ProductRepository;
import com.assignment.tamyuz.discount.service.persistence.repository.UserRepository;
import com.assignment.tamyuz.discount.service.utils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DiscountService discountService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = TestHelper.createDefaultUserWithUserRole();
        product = TestHelper.createDefaultProduct();
    }

    @Test
    void testPlaceOrder_success() {
        OrderRequest request = TestHelper.createDefaultOrderRequest();

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        // FIXED: use any(BigDecimal.class)
        when(discountService.calculateTotalDiscount(eq(user), any(BigDecimal.class)))
                .thenReturn(BigDecimal.TEN);

        Order savedOrder = Order.builder().id(1L).user(user).build();
        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        OrderResponse response = TestHelper.createDefaultOrderResponse();
        when(orderMapper.toResponse(savedOrder))
                .thenReturn(response);

        OrderResponse result = orderService.placeOrder(user.getEmail(), request.getItems());

        assertEquals(response, result);
        assertEquals(198, product.getQuantity()); // Stock decreased
    }

    @Test
    void testPlaceOrder_userNotFound() {
        when(userRepository.findByEmail("unknown")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> orderService.placeOrder("unknown", List.of()));
    }

    @Test
    void testPlaceOrder_productNotFound() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> orderService.placeOrder(user.getEmail(), List.of(new OrderItemRequest(1L, 1))));
    }

    @Test
    void testPlaceOrder_insufficientStock() {
        product.setQuantity(1);
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(BadRequestException.class,
                () -> orderService.placeOrder(user.getEmail(), List.of(new OrderItemRequest(1L, 2))));
    }
}

