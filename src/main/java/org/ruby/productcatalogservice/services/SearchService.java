package org.ruby.productcatalogservice.services;

import org.ruby.productcatalogservice.dtos.SortParam;
import org.ruby.productcatalogservice.models.Product;
import org.ruby.productcatalogservice.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService implements ISearchService {
    private final ProductRepository productRepository;

    public SearchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> searchProducts(String query, Integer pageNumber, Integer pageSize, List<SortParam> sortParams) {
        Sort sort = null;

        //Sort by price ascending and then by name descending
        if (!sortParams.isEmpty()) {
            if (sortParams.get(0).getOrder().equals("ASC")) {
                sort = Sort.by(sortParams.get(0).getParamName()).ascending();
            } else {
                sort = Sort.by(sortParams.get(0).getParamName()).descending();
            }

            for (int i = 1; i < sortParams.size(); i++) {
                if (sortParams.get(i).getOrder().equals("ASC")) {
                    sort = sort.and(Sort.by(sortParams.get(i).getParamName()).ascending());
                } else {
                    sort = sort.and(Sort.by(sortParams.get(i).getParamName()).descending());
                }
            }
        }

        return productRepository.findByDescription(query, PageRequest.of(pageNumber, pageSize, sort));
        //return productRepository.findByDescription(query, PageRequest.of(pageNumber, pageSize,Sort.by("price").ascending().and(Sort.by("description").descending())));
    }
}


/*
Pageable is an interface which has methods like getPageNo and
getPageSize which can be used,

PageRequest is one of Pageable implementations
 */
