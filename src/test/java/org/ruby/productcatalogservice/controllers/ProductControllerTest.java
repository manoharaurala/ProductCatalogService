package org.ruby.productcatalogservice.controllers;

import org.junit.jupiter.api.Test;
import org.ruby.productcatalogservice.dtos.ProductDTO;
import org.ruby.productcatalogservice.models.Product;
import org.ruby.productcatalogservice.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ProductController.class)
class ProductControllerTest {
    /*
    1.This test file containts unit tests for the ProductController class. We will use Mockito to mock the dependencies of the ProductController and test the controller methods.
    2.We test in isolation, so we will mock the ProductService and test the controller methods without actually calling the service methods. We will use Mockito to mock the ProductService and test the controller methods.
    3.
     */


    @Autowired
    private ProductController productController;

    @MockitoBean(name = "storageProductService")
    private IProductService productService;

    /*
    Happy case for getProductById method
    Arrange,Act,Assert
    Let’s create a product and add a condition that when
    productService.getProductById is called with the id
    which we pass in the product controller method
    then it has to return this newly created product.
     */

    @Test
    void testGetProductById_WithValidId_RunSuccessfully() {
        //1. Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setDescription("This is a test product");
        product.setPrice(100.0);
        when(productService.getProductById(productId)).thenReturn(product);

        //2. Act
        ResponseEntity<ProductDTO> response = productController.getProductById(productId);

        //3. Assert
        assertNotNull(response);
        assert (response.getStatusCode().value() == 200);
        assertNotNull(response.getBody());
        assertEquals(productId, response.getBody().getId());
        assertEquals(product.getName(), response.getBody().getName());
        assertEquals(product.getPrice(), response.getBody().getPrice());
        assertEquals(response.getBody().getDescription(), product.getDescription());

        /*
        The verify() method is used in Mockito to check whether a mocked method was called,
        how many times, and with what arguments during a test.
         */

        verify(productService, times(1)).getProductById(productId);


    }

    /*
    Test case for Sad scenario for getProductById method
    assertThrows(ExpectedException.class, () -> {
        // code that should throw the exception
    });
     */

    @Test
    public void testGetProductById_WithNegativeId_ThrowsIllegalArgumentException() {
        //1. Arrange
        Long productId = -1L;

        //2. Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            productController.getProductById(productId);
        });

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productController.getProductById(productId);
        });

        //3. Assert
        assertEquals("Product ID must be greater than 0", exception.getMessage());
        verify(productService, times(0)).getProductById(productId);
    }
}