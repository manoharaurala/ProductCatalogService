package org.ruby.productcatalogservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class FakestoreProductDto {
    private Long id;
    private String title;
    private String description;
    private String image;
    private Double price;
    private String category;


}
