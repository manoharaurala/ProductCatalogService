package org.ruby.productcatalogservice.repositories;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.ruby.productcatalogservice.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Transactional
    public void testFetchType(){
    /*
    1. EAGER Fetch Type: When we fetch a Category, all associated Products are fetched immediately.
          This is suitable when we always need the related entities along with the parent entity.
    2. LAZY Fetch Type: Products are fetched only when explicitly accessed.
     */
        Optional<Category> categoryOptional = categoryRepository.findById(1L);

        assertTrue(categoryOptional.isPresent(), "Category should exist");

        Category category = categoryOptional.get();
        assertNotNull(category, "Category should not be null");
        assertNotNull(category.getId(), "Category ID should not be null");

        // If using LAZY fetch, this line will trigger a SQL query to fetch products
        // If using EAGER fetch, products are already loaded

    }

    @Test
    @Transactional
    public void testNPlusOneProblem(){
        /*
        N+1 Problem: When fetching a list of Categories, if each Category has a list of Products, and the fetch type is LAZY,
        it can lead to N+1 queries. For example, if we fetch 10 Categories, and each Category has 5 Products, it will result in:
        - 1 query to fetch all Categories
        - 10 additional queries to fetch Products for each Category (N queries)
        Total: 1 + N = 11 queries
         */
        var categories = categoryRepository.findAll();
        assertFalse(categories.isEmpty(), "Categories should not be empty");

        // Access products for each category to trigger potential N+1 problem
        categories.forEach(category -> {
            var products = category.getProducts();
            assertNotNull(products, "Products should not be null");
        });
        /*
        LAZY+SELECT -> N+1 problem
        LAZY+SUBSELECT -> 2 queries
        EAGER+JOIN FETCH -> 1 query

        a. use lazy + sub select mode (2 queries)
        b. use batch fetching(n(batch size) size in parent table)
         if you specify batch size = n, we will see 1 + (N / n) queries
         */

        /*
        To mitigate the N+1 problem,
        1.Use FetchType.EAGER for the relationship (not always recommended due to performance concerns).
        2. Use JOIN FETCH in JPQL queries to fetch related entities in a single query.
        3. Use FetchType.LAZY and use FetchMode.SUBSELECT->2 queries instead of N+1.
        4.Use Batch Fetching to load related entities in batches.

         */
    }


}