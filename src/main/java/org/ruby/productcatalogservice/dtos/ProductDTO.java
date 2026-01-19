package org.ruby.productcatalogservice.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder(
        {
                "id",
                "name",
                "description",
                "price",
                "category",
                "imageUrl"
        }
)

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private CategoryDTO category;
    private String imageUrl;

}
