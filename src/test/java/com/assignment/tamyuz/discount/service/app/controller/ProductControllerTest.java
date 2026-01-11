package com.assignment.tamyuz.discount.service.app.controller;

import com.assignment.tamyuz.discount.service.domain.product.model.ProductRequest;
import com.assignment.tamyuz.discount.service.domain.product.model.ProductResponse;
import com.assignment.tamyuz.discount.service.domain.product.service.ProductService;
import com.assignment.tamyuz.discount.service.utils.PagedResponse;
import com.assignment.tamyuz.discount.service.utils.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    void testCreateProduct() {
        ProductRequest request = TestHelper.createDefaultProductRequest();
        ProductResponse response = TestHelper.createDefaultProductResponse();
        when(productService.create(request))
                .thenReturn(response);

        ProductResponse result = productController.create(request);

        assertEquals(response, result);
        verify(productService).create(request);
    }

    @Test
    void testListProducts() {
        PagedResponse<ProductResponse> paged = new PagedResponse<>(List.of(), 0, 10, 0, 10);
        when(productService.getAll(0, 10, "id"))
                .thenReturn(paged);

        PagedResponse<ProductResponse> result = productController.list(0, 10, "id");

        assertEquals(paged, result);
        verify(productService).getAll(0, 10, "id");
    }

    @Test
    void testGetById() {
        ProductResponse response = TestHelper.createDefaultProductResponse();
        when(productService.getById(1L))
                .thenReturn(response);

        ProductResponse result = productController.getById(1L);

        assertEquals(response, result);
        verify(productService).getById(1L);
    }

    @Test
    void testUpdate() {
        ProductRequest req = new ProductRequest("Updated", null, null, null);
        ProductResponse resp = TestHelper.createDefaultProductResponse();
        when(productService.update(1L, req))
                .thenReturn(resp);

        ProductResponse result = productController.update(1L, req);

        assertEquals(resp, result);
        verify(productService).update(1L, req);
    }

    @Test
    void testDelete() {
        doNothing().when(productService).delete(1L);

        productController.delete(1L);

        verify(productService).delete(1L);
    }
}

