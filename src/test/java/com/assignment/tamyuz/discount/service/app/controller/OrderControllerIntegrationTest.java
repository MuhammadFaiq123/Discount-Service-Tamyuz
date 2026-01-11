package com.assignment.tamyuz.discount.service.app.controller;

import com.assignment.tamyuz.discount.service.domain.order.model.OrderItemRequest;
import com.assignment.tamyuz.discount.service.domain.order.model.OrderRequest;
import com.assignment.tamyuz.discount.service.persistence.entity.Product;
import com.assignment.tamyuz.discount.service.persistence.entity.User;
import com.assignment.tamyuz.discount.service.persistence.repository.OrderRepository;
import com.assignment.tamyuz.discount.service.persistence.repository.ProductRepository;
import com.assignment.tamyuz.discount.service.persistence.repository.UserRepository;
import com.assignment.tamyuz.discount.service.utils.TestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();

        productRepository.deleteAll();
        userRepository.deleteAll();

        user = TestHelper.createDefaultUserWithUserRole();
        userRepository.save(user);

        product = TestHelper.createDefaultProduct();
        product.setId(null);
        productRepository.save(product);
    }

    @Test
    void testPlaceOrderEndpoint() throws Exception {
        OrderItemRequest item = new OrderItemRequest(product.getId(), 2);
        OrderRequest request = new OrderRequest(List.of(item));

        mockMvc.perform(post("/api/orders")
                        .with(jwt().jwt(jwt -> jwt.subject("userTemp@example.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .principal(() -> user.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testMyOrdersEndpoint() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .with(jwt().jwt(jwt -> jwt.subject("userTemp@example.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .principal(() -> user.getEmail()))
                .andExpect(status().isOk());
    }
}