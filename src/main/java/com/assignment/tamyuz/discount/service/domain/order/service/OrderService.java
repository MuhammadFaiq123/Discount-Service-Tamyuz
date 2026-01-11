package com.assignment.tamyuz.discount.service.domain.order.service;

import com.assignment.tamyuz.discount.service.app.exception.BadRequestException;
import com.assignment.tamyuz.discount.service.app.exception.NotFoundException;
import com.assignment.tamyuz.discount.service.domain.order.mapper.OrderMapper;
import com.assignment.tamyuz.discount.service.domain.order.model.OrderItemRequest;
import com.assignment.tamyuz.discount.service.domain.order.model.OrderResponse;
import com.assignment.tamyuz.discount.service.persistence.entity.Order;
import com.assignment.tamyuz.discount.service.persistence.entity.OrderItem;
import com.assignment.tamyuz.discount.service.persistence.entity.Product;
import com.assignment.tamyuz.discount.service.persistence.entity.User;
import com.assignment.tamyuz.discount.service.persistence.repository.OrderRepository;
import com.assignment.tamyuz.discount.service.persistence.repository.ProductRepository;
import com.assignment.tamyuz.discount.service.persistence.repository.UserRepository;
import com.assignment.tamyuz.discount.service.utils.PagedResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.assignment.tamyuz.discount.service.utils.HelperUtils.toPagedResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final DiscountService discountService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;


    public OrderResponse placeOrder(String userEmail, List<OrderItemRequest> requests) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        BigDecimal orderTotal = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();
        Order order = Order.builder()
                .user(user)
                .items(items)
                .build();

        for (OrderItemRequest req : requests) {

            Product product = productRepository.findById(req.productId())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + req.productId()));

            if (product.getQuantity() < req.quantity()) {
                throw new BadRequestException("Insufficient stock for product " + product.getName());
            }

            // Decrease inventory
            product.setQuantity(product.getQuantity() - req.quantity());

            BigDecimal unitPrice = product.getPrice();
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(req.quantity()));

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(req.quantity())
                    .unitPrice(unitPrice)
                    .discountApplied(BigDecimal.ZERO)
                    .totalPrice(totalPrice)
                    .build();

            items.add(item);
            orderTotal = orderTotal.add(totalPrice);
        }

        // Calculate discount dynamically
        BigDecimal totalDiscount = discountService.calculateTotalDiscount(user, orderTotal);

        // Apply discount proportionally to items
        if (totalDiscount.compareTo(BigDecimal.ZERO) > 0) {
            for (OrderItem item : items) {
                BigDecimal proportion =
                        item.getTotalPrice().divide(orderTotal, 4, RoundingMode.HALF_UP);
                BigDecimal itemDiscount = totalDiscount.multiply(proportion);
                item.setDiscountApplied(itemDiscount);
                item.setTotalPrice(item.getTotalPrice().subtract(itemDiscount));
            }
        }

        order.setOrderTotal(orderTotal.subtract(totalDiscount));

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    public PagedResponse<OrderResponse> getOrdersByUser(String userEmail, int page, int size, String sortBy, Sort.Direction direction) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Order> orderPage = orderRepository.findAllByUser(user, pageable);
        Page<OrderResponse> responsePage = orderPage.map(orderMapper::toResponse);

        return toPagedResponse(responsePage);
    }

}