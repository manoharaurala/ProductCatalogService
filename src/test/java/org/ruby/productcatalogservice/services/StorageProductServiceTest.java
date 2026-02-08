package org.ruby.productcatalogservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ruby.productcatalogservice.models.Category;
import org.ruby.productcatalogservice.models.Product;
import org.ruby.productcatalogservice.models.State;
import org.ruby.productcatalogservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = StorageProductService.class)
class StorageProductServiceTest {
    /*
    This test file contains unit tests for the StorageProductService class.
    We will use Mockito to mock the ProductRepository dependency and test the service methods in isolation.
     */

    @Autowired
    private StorageProductService storageProductService;

    @MockitoBean
    private ProductRepository productRepository;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        // Setup common test data
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Electronics");
        testCategory.setDescription("Electronic items");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(100.0);
        testProduct.setCategory(testCategory);
        testProduct.setImageUrl("http://test.com/image.jpg");
        testProduct.setState(State.ACTIVE);
        testProduct.setCreatedAt(new Date());
    }

    // ==================== getProductById Tests ====================

    @Test
    void testGetProductById_WithValidId_ReturnsProduct() {
        // Arrange
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));

        // Act
        Product result = storageProductService.getProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals(testProduct.getName(), result.getName());
        assertEquals(testProduct.getDescription(), result.getDescription());
        assertEquals(testProduct.getPrice(), result.getPrice());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetProductById_WithNonExistentId_ReturnsNull() {
        // Arrange
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Product result = storageProductService.getProductById(productId);

        // Assert
        assertNull(result);
        verify(productRepository, times(1)).findById(productId);
    }

    // ==================== getProducts Tests ====================

    @Test
    void testGetProducts_ReturnsAllProducts() {
        // Arrange
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(200.0);

        List<Product> productList = new ArrayList<>();
        productList.add(testProduct);
        productList.add(product2);

        when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<Product> result = storageProductService.getProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testProduct.getName(), result.get(0).getName());
        assertEquals(product2.getName(), result.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProducts_WhenEmpty_ReturnsEmptyList() {
        // Arrange
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Product> result = storageProductService.getProducts();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(productRepository, times(1)).findAll();
    }

    // ==================== createProduct Tests ====================

    @Test
    void testCreateProduct_WithNewProduct_SavesSuccessfully() {
        // Arrange
        Product newProduct = new Product();
        newProduct.setId(2L);
        newProduct.setName("New Product");
        newProduct.setPrice(150.0);

        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        when(productRepository.save(newProduct)).thenReturn(newProduct);

        // Act
        Product result = storageProductService.createProduct(newProduct);

        // Assert
        assertNotNull(result);
        assertEquals(newProduct.getId(), result.getId());
        assertEquals(newProduct.getName(), result.getName());
        verify(productRepository, times(1)).findById(2L);
        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    void testCreateProduct_WithExistingId_ReturnsNull() {
        // Arrange
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));

        // Act
        Product result = storageProductService.createProduct(testProduct);

        // Assert
        assertNull(result);
        verify(productRepository, times(1)).findById(testProduct.getId());
        verify(productRepository, times(0)).save(any(Product.class));
    }

    // ==================== replaceProduct Tests ====================

    @Test
    void testReplaceProduct_WithValidProduct_ReplacesSuccessfully() {
        // Arrange
        Long productId = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(200.0);
        updatedProduct.setCategory(testCategory);
        updatedProduct.setImageUrl("http://updated.com/image.jpg");

        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Product result = storageProductService.replaceProduct(updatedProduct, productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Updated Product", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(200.0, result.getPrice());
        assertEquals(testProduct.getCreatedAt(), result.getCreatedAt());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testReplaceProduct_WithNonExistentId_ReturnsNull() {
        // Arrange
        Long productId = 999L;
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(200.0);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Product result = storageProductService.replaceProduct(updatedProduct, productId);

        // Assert
        assertNull(result);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void testReplaceProduct_WithNullProduct_ThrowsIllegalArgumentException() {
        // Arrange
        Long productId = 1L;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            storageProductService.replaceProduct(null, productId);
        });

        verify(productRepository, times(0)).findById(any());
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void testReplaceProduct_WithEmptyProduct_ThrowsIllegalArgumentException() {
        // Arrange
        Long productId = 1L;
        Product emptyProduct = new Product();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            storageProductService.replaceProduct(emptyProduct, productId);
        });

        assertEquals("Product data cannot be empty", exception.getMessage());
        verify(productRepository, times(0)).findById(any());
        verify(productRepository, times(0)).save(any(Product.class));
    }

    // ==================== deleteProduct Tests ====================

    @Test
    void testDeleteProduct_WithActiveProduct_DeletesSuccessfully() {
        // Arrange
        Long productId = 1L;
        testProduct.setState(State.ACTIVE);

        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Boolean result = storageProductService.deleteProduct(productId);

        // Assert
        assertTrue(result);
        assertEquals(State.INACTIVE, testProduct.getState());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void testDeleteProduct_WithInactiveProduct_ReturnsFalse() {
        // Arrange
        Long productId = 1L;
        testProduct.setState(State.INACTIVE);

        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));

        // Act
        Boolean result = storageProductService.deleteProduct(productId);

        // Assert
        assertFalse(result);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_WithNonExistentId_ReturnsFalse() {
        // Arrange
        Long productId = 999L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Boolean result = storageProductService.deleteProduct(productId);

        // Assert
        assertFalse(result);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(0)).save(any(Product.class));
    }
}

