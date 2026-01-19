package org.ruby.productcatalogservice.services;

import org.ruby.productcatalogservice.dtos.FakestoreProductDto;
import org.ruby.productcatalogservice.mappers.FakestoreProductMapper;
import org.ruby.productcatalogservice.models.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakestoreProductService implements IProductService {
    private final RestTemplate restTemplate;

    public FakestoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductById(Long id) {
        /*FakestoreProductDto fakestoreProductDto = restTemplate.getForObject("https://fakestoreapi.com/products/{productId}",
                FakestoreProductDto.class,
                id);
        if (fakestoreProductDto != null) {
            return FakestoreProductMapper.mapToProduct(fakestoreProductDto);
        }*/

        ResponseEntity<FakestoreProductDto> fakestoreProductDtoResponseEntity = restTemplate.getForEntity("https://fakestoreapi.com/products/{productId}",
                FakestoreProductDto.class,
                id);
        if (fakestoreProductDtoResponseEntity.getStatusCode().is2xxSuccessful()) {
            FakestoreProductDto fakestoreProductDto = fakestoreProductDtoResponseEntity.getBody();
            if (fakestoreProductDto != null) {
                return FakestoreProductMapper.mapToProduct(fakestoreProductDto);
            }
        }

        return null;
    }

    @Override
    public List<Product> getProducts() {

        /*FakestoreProductDto[] fakestoreProductDtos = restTemplate.getForObject("https://fakestoreapi.com/products",
                FakestoreProductDto[].class);

         */

        ResponseEntity<FakestoreProductDto[]> fakestoreProductDtoResponseEntity = restTemplate.getForEntity("https://fakestoreapi.com/products", FakestoreProductDto[].class);
        FakestoreProductDto[] fakestoreProductDtos = null;
        if (fakestoreProductDtoResponseEntity.getStatusCode().is2xxSuccessful()) {
            fakestoreProductDtos = fakestoreProductDtoResponseEntity.getBody();
        }
        List<Product> products = new ArrayList<>();
        if (fakestoreProductDtos != null) {
            for (FakestoreProductDto fakestoreProductDto : fakestoreProductDtos) {
                products.add(FakestoreProductMapper.mapToProduct(fakestoreProductDto));
            }
        }
        return products;

    }

    @Override
    public Product createProduct(Product product) {
        Product createdProduct = new Product();
        createdProduct.setId(product.getId());
        createdProduct.setName(product.getName());
        createdProduct.setDescription(product.getDescription());
        createdProduct.setPrice(product.getPrice());
        createdProduct.setImageUrl(product.getImageUrl());

        return createdProduct;

    }
}
