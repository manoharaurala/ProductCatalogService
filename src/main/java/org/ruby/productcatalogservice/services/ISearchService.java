package org.ruby.productcatalogservice.services;

import org.ruby.productcatalogservice.dtos.SortParam;
import org.ruby.productcatalogservice.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISearchService {
    /*
    Search products based on query and pagination parameters
    */
    Page<Product> searchProducts(String query, Integer pageNumber, Integer pageSize, List<SortParam> sortParams);
}
