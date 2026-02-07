package org.ruby.productcatalogservice.controllers;

import org.junit.jupiter.api.Test;
import org.ruby.productcatalogservice.dtos.ProductDTO;
import org.ruby.productcatalogservice.models.Product;
import org.ruby.productcatalogservice.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*MockMvc tests are a way to test your Spring MVC controllers
without starting the full server.

They simulate HTTP requests (GET, POST, PUT…) and let you test:
- Controller endpoints
- Request validation
- Request/response structure
- HTTP status codes
- JSON serialization/deserialization
- Interactions with services (via mocks)


They are fast, lightweight, and focused.

We use @WebMvcTest to test controllers because it loads only the MVC layer,
making tests faster, isolated, and focused on request/response handling.

@SpringBootTest loads the entire application, which is slow and turns
the test into a full integration test instead of a controller unit test.

*/

/*
    In earlier scenario:
    we were testing method logic
    we were directly calling method from tests using object of controller


    GET /products

    List<ProductDTO> response
    "[
        {
            "name" : "iPhone 14",
            "description" : "Apple iPhone 14 with A15 Bionic chip",
            "price" : 999.99
            ..
        },
        {
            "name" : "Samsung Galaxy S22",
            "description" : "Samsung Galaxy S22 with Exynos 2200",
            "price" : 899.99
            ..
        },
            ...
    ]"


    POST /products
    "{
    "name" : "iPhone 14",
    "description" : "Apple iPhone 14 with A15 Bionic chip",
    "price" : 999.99
    }"
     gets converted into a ProductDTO

     */


@WebMvcTest(ProductController.class)
public class ProductControllerMvcTest {
    /*
    Test the product controller web apis
    */


    @MockitoBean(value = "storageProductService")//@MockBean is deprecated
    private IProductService productService;

    @Autowired
    private MockMvc mockMvc;

    /*
    getALlProducts
    /products
     */

    @Test
    public void testGetAllProducts_RunSuccessfully() throws Exception {
        //mockMvc.perform(get("/products")).andExpect(status().isOk());
        /*
    using mockmvc, we are not actually testing the methods
    rather we are just testing the endpoints, that's it

    In previous types of tests, we were testing logic of methods,
    that's why we actually used the product controller and called the methods
    to run the actual code
     */
        //1. Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("This is a test product");
        product.setPrice(100.0);

        List<Product> products = new ArrayList<>();
        products.add(product);

        when(productService.getProducts()).thenReturn(products);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());

        List<ProductDTO> productDTOs = new ArrayList<>();
        productDTOs.add(productDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String apiResponse = objectMapper.writeValueAsString(productDTOs);

        //2. Act and Assert
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertEquals(apiResponse, response);
                });


    }


}
