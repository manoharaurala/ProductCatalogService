package org.ruby.productcatalogservice.mappers;

import org.ruby.productcatalogservice.dtos.ProductDTO;
import org.ruby.productcatalogservice.models.Product;

public class ProductMapper {
    private ProductMapper() {
    }

    public static ProductDTO mapToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setImageUrl(product.getImageUrl());
        if (product.getCategory() != null) {
            productDTO.setCategory(CategoryMapper.mapToCategoryDTO(product.getCategory()));
        }
        return productDTO;
    }

    public static Product mapToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        if (productDTO.getCategory() != null) {
            product.setCategory(CategoryMapper.mapToCategory(productDTO.getCategory()));
        }
        return product;
    }
}
