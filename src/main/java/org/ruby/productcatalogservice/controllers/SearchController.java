package org.ruby.productcatalogservice.controllers;

import org.ruby.productcatalogservice.dtos.SearchRequestDTO;
import org.ruby.productcatalogservice.models.Product;
import org.ruby.productcatalogservice.services.ISearchService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final ISearchService searchService;

    public SearchController(ISearchService searchService) {
        this.searchService = searchService;
    }


    /*
    Search
    type:Post
    we have lot of things to pass in the request

    DTO for search request

     */

    @PostMapping
    public Page<Product> search(@RequestBody SearchRequestDTO requestDTO) {
        Page<Product> products = searchService.searchProducts(
                requestDTO.getQuery(),
                requestDTO.getPageNumber(),
                requestDTO.getPageSize(),
                requestDTO.getSortParams());
        return products;
    }
}
