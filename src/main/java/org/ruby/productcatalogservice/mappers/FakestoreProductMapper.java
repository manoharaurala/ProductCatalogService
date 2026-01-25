package org.ruby.productcatalogservice.mappers;

import org.ruby.productcatalogservice.dtos.FakestoreProductDto;
import org.ruby.productcatalogservice.models.Category;
import org.ruby.productcatalogservice.models.Product;

public class FakestoreProductMapper {
    private FakestoreProductMapper() {
    }

    public static Product mapToProduct(FakestoreProductDto fakestoreProductDto) {
        Product product = new Product();
        product.setId(fakestoreProductDto.getId());
        product.setName(fakestoreProductDto.getTitle());
        product.setDescription(fakestoreProductDto.getDescription());
        product.setImageUrl(fakestoreProductDto.getImage());
        product.setPrice(fakestoreProductDto.getPrice());
        /*
        fakestore category is a string
        but in our product, categorys is not a string
         */

        Category category = new Category();
        category.setName(fakestoreProductDto.getCategory());
        product.setCategory(category);

        return product;

    }

    public static FakestoreProductDto mapToFakestoreProductDto(Product product) {
        FakestoreProductDto fakestoreProductDto = new FakestoreProductDto();
        fakestoreProductDto.setId(product.getId());
        fakestoreProductDto.setTitle(product.getName());
        fakestoreProductDto.setDescription(product.getDescription());
        fakestoreProductDto.setImage(product.getImageUrl());
        fakestoreProductDto.setPrice(product.getPrice());
        /*
        fakestore category is a string
        but in our product, categorys is not a string
         */

        if (product.getCategory() != null) {
            fakestoreProductDto.setCategory(product.getCategory().getName());
        }

        return fakestoreProductDto;
    }

}
