package org.ruby.productcatalogservice.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product extends BaseModel {
    private String name;
    private String description;
    private Double price;
    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;
    private String imageUrl;

}

/*
Product Category
Product-> Category 1:1
Category-> Product 1:M

Cardanality: Many-to-One => Gets converted to foreign key constraint in the table
Cascade Type: ALL, PERSIST, MERGE, REMOVE, REFRESH, DETACH
Fetch Type: EAGER, LAZY
 */
