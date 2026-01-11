package com.assignment.tamyuz.discount.service.domain.product.service;

import com.assignment.tamyuz.discount.service.app.exception.NotFoundException;
import com.assignment.tamyuz.discount.service.domain.product.model.ProductRequest;
import com.assignment.tamyuz.discount.service.domain.product.model.ProductResponse;
import com.assignment.tamyuz.discount.service.persistence.entity.Product;
import com.assignment.tamyuz.discount.service.persistence.repository.ProductRepository;
import com.assignment.tamyuz.discount.service.utils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = TestHelper.createDefaultProduct();
    }

    @Test
    void testCreateProduct() {
        ProductRequest request = new ProductRequest("P", "D", BigDecimal.TEN, 5);
        when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        ProductResponse resp = productService.create(request);

        assertEquals(product.getName(), resp.getName());
    }

    @Test
    void testGetById_success() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        ProductResponse resp = productService.getById(1L);
        assertEquals("Default Name", resp.getName());
    }

    @Test
    void testGetById_notFound() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getById(1L));
    }

    @Test
    void testUpdate() {
        ProductRequest req = new ProductRequest("Updated", null, null, null);
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        when(productRepository.save(product))
                .thenReturn(product);

        ProductResponse resp = productService.update(1L, req);

        assertEquals("Updated", resp.getName());
    }

    @Test
    void testDelete() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        doNothing().when(productRepository).softDelete(1L);
        productService.delete(1L);
        verify(productRepository).softDelete(1L);
    }
}

