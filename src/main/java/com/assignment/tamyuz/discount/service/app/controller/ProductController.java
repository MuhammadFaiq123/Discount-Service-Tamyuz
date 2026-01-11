package com.assignment.tamyuz.discount.service.app.controller;

import com.assignment.tamyuz.discount.service.domain.product.model.ProductRequest;
import com.assignment.tamyuz.discount.service.domain.product.model.ProductResponse;
import com.assignment.tamyuz.discount.service.domain.product.service.ProductService;
import com.assignment.tamyuz.discount.service.utils.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product catalog APIs")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create product (ADMIN)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Product created")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProductResponse create(@RequestBody @Valid ProductRequest request) {
        return productService.create(request);
    }


    @Operation(summary = "List products (public)")
    @ApiResponse(responseCode = "200", description = "Product list returned")
    @GetMapping
    public PagedResponse<ProductResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        return productService.getAll(page, size, sort);
    }

    @Operation(summary = "Get product by ID (public)")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @Operation(summary = "Update product (ADMIN)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Product updated")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductResponse update(
            @PathVariable Long id,
            @RequestBody @Valid ProductRequest request
    ) {
        return productService.update(id, request);
    }

    @Operation(summary = "Delete product (ADMIN)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "204", description = "Product deleted")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}