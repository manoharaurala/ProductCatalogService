package org.ruby.productcatalogservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ruby.productcatalogservice.clients.FakestoreAPIClient;
import org.ruby.productcatalogservice.dtos.FakestoreProductDto;
import org.ruby.productcatalogservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = FakestoreProductService.class)
class FakestoreProductServiceTest {
    /*
    This test file contains unit tests for the FakestoreProductService class.
    We will use Mockito to mock the FakestoreAPIClient and WebClient dependencies
    and test the service methods in isolation.
     */

    @Autowired
    private FakestoreProductService fakestoreProductService;

    @MockitoBean
    private FakestoreAPIClient fakestoreAPIClient;

    @MockitoBean
    private WebClient webClient;

    private FakestoreProductDto testFakestoreProductDto;

    @BeforeEach
    void setUp() {
        // Setup common test data
        testFakestoreProductDto = new FakestoreProductDto();
        testFakestoreProductDto.setId(1L);
        testFakestoreProductDto.setTitle("Test Product");
        testFakestoreProductDto.setDescription("Test Description");
        testFakestoreProductDto.setPrice(100.0);
        testFakestoreProductDto.setCategory("Electronics");
        testFakestoreProductDto.setImage("http://test.com/image.jpg");
    }

    // ==================== getProductById Tests ====================

    @Test
    void testGetProductById_WithValidId_ReturnsProduct() {
        // Arrange
        Long productId = 1L;
        ResponseEntity<FakestoreProductDto> responseEntity = ResponseEntity.ok(testFakestoreProductDto);

        when(fakestoreAPIClient.getForEntity(
                anyString(),
                eq(FakestoreProductDto.class),
                eq(productId)
        )).thenReturn(responseEntity);

        when(fakestoreAPIClient.validateResponse(responseEntity)).thenReturn(true);

        // Act
        Product result = fakestoreProductService.getProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals(testFakestoreProductDto.getId(), result.getId());
        assertEquals(testFakestoreProductDto.getTitle(), result.getName());
        assertEquals(testFakestoreProductDto.getDescription(), result.getDescription());
        assertEquals(testFakestoreProductDto.getPrice(), result.getPrice());
        assertEquals(testFakestoreProductDto.getImage(), result.getImageUrl());
        verify(fakestoreAPIClient, times(1)).getForEntity(
                anyString(),
                eq(FakestoreProductDto.class),
                eq(productId)
        );
        verify(fakestoreAPIClient, times(1)).validateResponse(responseEntity);
    }

    @Test
    void testGetProductById_WithInvalidResponse_ReturnsNull() {
        // Arrange
        Long productId = 1L;
        ResponseEntity<FakestoreProductDto> responseEntity = ResponseEntity.status(404).build();

        when(fakestoreAPIClient.getForEntity(
                anyString(),
                eq(FakestoreProductDto.class),
                eq(productId)
        )).thenReturn(responseEntity);

        when(fakestoreAPIClient.validateResponse(responseEntity)).thenReturn(false);

        // Act
        Product result = fakestoreProductService.getProductById(productId);

        // Assert
        assertNull(result);
        verify(fakestoreAPIClient, times(1)).getForEntity(
                anyString(),
                eq(FakestoreProductDto.class),
                eq(productId)
        );
        verify(fakestoreAPIClient, times(1)).validateResponse(responseEntity);
    }

    @Test
    void testGetProductById_WithNullBody_ReturnsNull() {
        // Arrange
        Long productId = 1L;
        ResponseEntity<FakestoreProductDto> responseEntity = ResponseEntity.ok().build();

        when(fakestoreAPIClient.getForEntity(
                anyString(),
                eq(FakestoreProductDto.class),
                eq(productId)
        )).thenReturn(responseEntity);

        when(fakestoreAPIClient.validateResponse(responseEntity)).thenReturn(false);

        // Act
        Product result = fakestoreProductService.getProductById(productId);

        // Assert
        assertNull(result);
        verify(fakestoreAPIClient, times(1)).validateResponse(responseEntity);
    }

    // ==================== getProducts Tests ====================

    @Test
    void testGetProducts_ReturnsProductList() {
        // Arrange
        FakestoreProductDto dto1 = new FakestoreProductDto();
        dto1.setId(1L);
        dto1.setTitle("Product 1");
        dto1.setPrice(100.0);

        FakestoreProductDto dto2 = new FakestoreProductDto();
        dto2.setId(2L);
        dto2.setTitle("Product 2");
        dto2.setPrice(200.0);

        FakestoreProductDto[] dtoArray = {dto1, dto2};
        ResponseEntity<FakestoreProductDto[]> responseEntity = ResponseEntity.ok(dtoArray);

        when(fakestoreAPIClient.getForEntity(
                anyString(),
                eq(FakestoreProductDto[].class)
        )).thenReturn(responseEntity);

        // Act
        List<Product> result = fakestoreProductService.getProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
        assertEquals(100.0, result.get(0).getPrice());
        assertEquals(200.0, result.get(1).getPrice());
        verify(fakestoreAPIClient, times(1)).getForEntity(
                anyString(),
                eq(FakestoreProductDto[].class)
        );
    }

    @Test
    void testGetProducts_WithEmptyResponse_ReturnsEmptyList() {
        // Arrange
        FakestoreProductDto[] emptyArray = {};
        ResponseEntity<FakestoreProductDto[]> responseEntity = ResponseEntity.ok(emptyArray);

        when(fakestoreAPIClient.getForEntity(
                anyString(),
                eq(FakestoreProductDto[].class)
        )).thenReturn(responseEntity);

        // Act
        List<Product> result = fakestoreProductService.getProducts();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(fakestoreAPIClient, times(1)).getForEntity(
                anyString(),
                eq(FakestoreProductDto[].class)
        );
    }

    @Test
    void testGetProducts_WithNon200Status_ReturnsNull() {
        // Arrange
        ResponseEntity<FakestoreProductDto[]> responseEntity =
            ResponseEntity.status(HttpStatusCode.valueOf(500)).build();

        when(fakestoreAPIClient.getForEntity(
                anyString(),
                eq(FakestoreProductDto[].class)
        )).thenReturn(responseEntity);

        // Act
        List<Product> result = fakestoreProductService.getProducts();

        // Assert
        assertNull(result);
        verify(fakestoreAPIClient, times(1)).getForEntity(
                anyString(),
                eq(FakestoreProductDto[].class)
        );
    }

    @Test
    void testGetProducts_WithNullBody_ReturnsNull() {
        // Arrange
        ResponseEntity<FakestoreProductDto[]> responseEntity =
            ResponseEntity.ok().build();

        when(fakestoreAPIClient.getForEntity(
                anyString(),
                eq(FakestoreProductDto[].class)
        )).thenReturn(responseEntity);

        // Act
        List<Product> result = fakestoreProductService.getProducts();

        // Assert
        assertNull(result);
        verify(fakestoreAPIClient, times(1)).getForEntity(
                anyString(),
                eq(FakestoreProductDto[].class)
        );
    }

    // ==================== createProduct Tests ====================

    @Test
    void testCreateProduct_WithValidProduct_CreatesSuccessfully() {
        // Arrange
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setDescription("New Description");
        newProduct.setPrice(150.0);

        FakestoreProductDto createdDto = new FakestoreProductDto();
        createdDto.setId(3L);
        createdDto.setTitle("New Product");
        createdDto.setDescription("New Description");
        createdDto.setPrice(150.0);

        // Mock WebClient chain
        WebClient.RequestBodyUriSpec requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FakestoreProductDto.class)).thenReturn(Mono.just(createdDto));

        // Act
        Product result = fakestoreProductService.createProduct(newProduct);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("New Product", result.getName());
        assertEquals("New Description", result.getDescription());
        assertEquals(150.0, result.getPrice());
        verify(webClient, times(1)).post();
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

        FakestoreProductDto updatedDto = new FakestoreProductDto();
        updatedDto.setId(productId);
        updatedDto.setTitle("Updated Product");
        updatedDto.setDescription("Updated Description");
        updatedDto.setPrice(200.0);

        ResponseEntity<FakestoreProductDto> responseEntity = ResponseEntity.ok(updatedDto);

        when(fakestoreAPIClient.putForEntity(
                anyString(),
                any(FakestoreProductDto.class),
                eq(FakestoreProductDto.class),
                eq(productId)
        )).thenReturn(responseEntity);

        // Act
        Product result = fakestoreProductService.replaceProduct(updatedProduct, productId);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(200.0, result.getPrice());
        verify(fakestoreAPIClient, times(1)).putForEntity(
                anyString(),
                any(FakestoreProductDto.class),
                eq(FakestoreProductDto.class),
                eq(productId)
        );
    }

    @Test
    void testReplaceProduct_WithNon200Status_ReturnsNull() {
        // Arrange
        Long productId = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(200.0);

        ResponseEntity<FakestoreProductDto> responseEntity =
            ResponseEntity.status(HttpStatusCode.valueOf(404)).build();

        when(fakestoreAPIClient.putForEntity(
                anyString(),
                any(FakestoreProductDto.class),
                eq(FakestoreProductDto.class),
                eq(productId)
        )).thenReturn(responseEntity);

        // Act
        Product result = fakestoreProductService.replaceProduct(updatedProduct, productId);

        // Assert
        assertNull(result);
        verify(fakestoreAPIClient, times(1)).putForEntity(
                anyString(),
                any(FakestoreProductDto.class),
                eq(FakestoreProductDto.class),
                eq(productId)
        );
    }

    @Test
    void testReplaceProduct_WithNullBody_ReturnsNull() {
        // Arrange
        Long productId = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(200.0);

        ResponseEntity<FakestoreProductDto> responseEntity = ResponseEntity.ok().build();

        when(fakestoreAPIClient.putForEntity(
                anyString(),
                any(FakestoreProductDto.class),
                eq(FakestoreProductDto.class),
                eq(productId)
        )).thenReturn(responseEntity);

        // Act
        Product result = fakestoreProductService.replaceProduct(updatedProduct, productId);

        // Assert
        assertNull(result);
        verify(fakestoreAPIClient, times(1)).putForEntity(
                anyString(),
                any(FakestoreProductDto.class),
                eq(FakestoreProductDto.class),
                eq(productId)
        );
    }
}

