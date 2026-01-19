package org.ruby.productcatalogservice.dtos;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class FakestoreProductDto {
    private Long id;
    private String title;
    private String description;
    private String image;
    private Double price;
    private String category;


}
