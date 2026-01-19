package org.ruby.productcatalogservice.services;

import org.ruby.productcatalogservice.models.Product;

import java.util.List;

public interface IProductService {
    Product getProductById(Long id);

    List<Product> getProducts();

    Product createProduct(Product product);
}
