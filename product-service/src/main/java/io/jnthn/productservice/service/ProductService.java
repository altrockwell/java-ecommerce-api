package io.jnthn.productservice.service;

import io.jnthn.productservice.dto.ProductRequest;
import io.jnthn.productservice.dto.ProductResponse;
import io.jnthn.productservice.model.Product;
import io.jnthn.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        return mapToProductResponse(productRepository.save(product));
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
