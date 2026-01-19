package org.ruby.productcatalogservice.controllers;

import org.ruby.productcatalogservice.dtos.ProductDTO;
import org.ruby.productcatalogservice.mappers.ProductMapper;
import org.ruby.productcatalogservice.models.Product;
import org.ruby.productcatalogservice.services.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final IProductService productService;

    /*
    Constructor-based dependency injection
     */
    public ProductController(IProductService productService) {
        this.productService = productService;
    }
    /*
    1. Create Product
    2. Get Product by ID
    3. Get All Products
     */

    /*
    Create product
     */
    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO productDto) {
        /*
        Call service to create product
         */
        Product product = productService.createProduct(ProductMapper.mapToProduct(productDto));
        return ProductMapper.mapToProductDTO(product);

    }

    /*
    Get product by ID
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("productId") Long productId) {
        /*
        Call service to get product by ID
         */
        if (productId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Product product = productService.getProductById(productId);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ProductMapper.mapToProductDTO(product), HttpStatus.OK);

    }

    /*
    Get all products
     */

    @GetMapping
    public List<ProductDTO> getProducts() {
        /*
        Call service to get all products
         */
        List<Product> products = productService.getProducts();
        return
                products.stream()
                        .map(ProductMapper::mapToProductDTO)
                        .toList();

    }


}
