package org.ruby.productcatalogservice.controllers;

import org.ruby.productcatalogservice.dtos.ProductRequestDto;
import org.ruby.productcatalogservice.dtos.ProductResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    /*
    1. Create Product
    2. Get Product by ID
    3. Get All Products
     */

    /*
    Create product
     */
    @PostMapping("")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto productDto) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        /*
        Call service to create product
         */
        return productResponseDto;

    }

    /*
    Get product by ID
     */
    @GetMapping("/{id}")
    public ProductResponseDto getProductById(Long id) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        /*
        Call service to get product by ID
         */
        return productResponseDto;

    }

    /*
    Get all products
     */

    @GetMapping("")
    public List<ProductResponseDto> getProducts() {
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        /*
        Call service to get all products
         */
        return productResponseDtos;

    }


}
