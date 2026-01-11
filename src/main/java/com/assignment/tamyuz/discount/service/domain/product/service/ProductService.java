package com.assignment.tamyuz.discount.service.domain.product.service;

import com.assignment.tamyuz.discount.service.app.exception.NotFoundException;
import com.assignment.tamyuz.discount.service.domain.product.model.ProductRequest;
import com.assignment.tamyuz.discount.service.domain.product.model.ProductResponse;
import com.assignment.tamyuz.discount.service.persistence.entity.Product;
import com.assignment.tamyuz.discount.service.persistence.repository.ProductRepository;
import com.assignment.tamyuz.discount.service.utils.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.assignment.tamyuz.discount.service.utils.HelperUtils.toPagedResponse;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public PagedResponse<ProductResponse> getAll(int page, int size, String sort) {

        Sort sortObj = Sort.by(sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<ProductResponse> pageResult = productRepository.findAll(pageable)
                .map(this::toResponse);

        return toPagedResponse(pageResult);
    }

    public ProductResponse create(ProductRequest request) {

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    public ProductResponse getById(Long id) {
        return productRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public ProductResponse update(Long id, ProductRequest request) {

        Product product = findProductById(id);
        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getQuantity() != null) product.setQuantity(request.getQuantity());

        Product updated = productRepository.save(product);
        return toResponse(updated);
    }

    public void delete(Long id) {
        findProductById(id);
        productRepository.softDelete(id);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }
}