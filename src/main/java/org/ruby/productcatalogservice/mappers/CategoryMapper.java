package org.ruby.productcatalogservice.mappers;

import org.ruby.productcatalogservice.dtos.CategoryDTO;
import org.ruby.productcatalogservice.models.Category;

public class CategoryMapper {
    private CategoryMapper() {
    }

    public static CategoryDTO mapToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    public static Category mapToCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        return category;
    }
}
