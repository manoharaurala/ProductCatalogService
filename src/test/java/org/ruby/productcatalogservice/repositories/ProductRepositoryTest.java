package org.ruby.productcatalogservice.repositories;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.ruby.productcatalogservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    // Test cases would go here
    public void testRepositoryMethod() {
        /*
        Get all products in price range
         */
        List<Product> products = productRepository.findProductsByPriceBetween(50000.0, 80000.0);
        assertEquals(3, products.size());
    }

    @Test
    @Transactional
    public void testFindAllProductsWhereNameLike() {
        List<Product> products = productRepository.findAllProductsWhereNameLike("MacBook Air");
        assertEquals(4, products.size());
        assertEquals("MacBook Air M4", products.get(3).getName());
    }

    @Test
    @Transactional
    public void testFindProductsOrderByPriceAsc() {
        List<Product> products = productRepository.findAllByOrderByPriceAsc();
        assertEquals(4, products.size());
        assertTrue(products.get(0).getPrice() <= products.get(1).getPrice());
        assertTrue(products.get(1).getPrice() <= products.get(2).getPrice());
        assertTrue(products.get(2).getPrice() <= products.get(3).getPrice());
    }

    @Test
    @Transactional
    public void testFindDescriptionWhereIdIs() {
        String description = productRepository.findDescriptionWhereIdIs(1L);
        assertEquals("Macbook from Apple", description);
    }

}