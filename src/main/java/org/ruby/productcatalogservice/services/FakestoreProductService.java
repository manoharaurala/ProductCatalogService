package org.ruby.productcatalogservice.services;

import org.ruby.productcatalogservice.clients.FakestoreAPIClient;
import org.ruby.productcatalogservice.dtos.FakestoreProductDto;
import org.ruby.productcatalogservice.mappers.FakestoreProductMapper;
import org.ruby.productcatalogservice.models.Product;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakestoreProductService implements IProductService {

    private final WebClient webClient;
    private final FakestoreAPIClient fakestoreAPIClient;

    public FakestoreProductService(FakestoreAPIClient fakestoreAPIClient, WebClient webClient) {
        this.fakestoreAPIClient = fakestoreAPIClient;
        this.webClient = webClient;
    }

    @Override
    public Product getProductById(Long id) {
        /*FakestoreProductDto fakestoreProductDto = restTemplate.getForObject("https://fakestoreapi.com/products/{productId}",
                FakestoreProductDto.class,
                id);
        if (fakestoreProductDto != null) {
            return FakestoreProductMapper.mapToProduct(fakestoreProductDto);
        }*/

        /*ResponseEntity<FakestoreProductDto> fakestoreProductDtoResponseEntity = restTemplate.getForEntity("https://fakestoreapi.com/products/{productId}",
                FakestoreProductDto.class,
                id);
        if (fakestoreProductDtoResponseEntity.getStatusCode().is2xxSuccessful()) {
            FakestoreProductDto fakestoreProductDto = fakestoreProductDtoResponseEntity.getBody();
            if (fakestoreProductDto != null) {
                return FakestoreProductMapper.mapToProduct(fakestoreProductDto);
            }
        }

        return null;*/

        ResponseEntity<FakestoreProductDto> fakestoreProductDtoResponseEntity = fakestoreAPIClient.getForEntity(
                "https://fakestoreapi.com/products/{id}",
                FakestoreProductDto.class,
                id
        );


        if (fakestoreAPIClient.validateResponse(fakestoreProductDtoResponseEntity)) {
            return FakestoreProductMapper.mapToProduct(fakestoreProductDtoResponseEntity.getBody());
        }

        return null;
    }

    @Override
    public List<Product> getProducts() {

        /*FakestoreProductDto[] fakestoreProductDtos = restTemplate.getForObject("https://fakestoreapi.com/products",
                FakestoreProductDto[].class);

         */
        /*
    Generics?
    <T>
    Type erasure
    1. Compiling(Writing code)
    2. Runtime(Running code)

    List<String>
    List<Integer>
    Java checks your code to make sure you are not putting
    an integer into a string list. very strict.

    However, as soon as you compile the code, java "erases" the specific
    type inside the brackets

    List<T> ls = new List<>();


    List => class

    List<Integer> just becomes a list for Java
    List<String> just becomes a list for Java

    Rest templare needs to know exactly what class to create.
    Solution - Use arrays as they don't suffer from type erasure.

    Why do we have type erasure? - concept of generics.



    PUT - replaces/updates the entire product
    PATCH - partially updates the product
     */

        /*ResponseEntity<FakestoreProductDto[]> fakestoreProductDtoResponseEntity = restTemplate.getForEntity("https://fakestoreapi.com/products", FakestoreProductDto[].class);

        FakestoreProductDto[] fakestoreProductDtos = null;
        if (fakestoreProductDtoResponseEntity.getStatusCode().is2xxSuccessful()) {
            fakestoreProductDtos = fakestoreProductDtoResponseEntity.getBody();
        }
        List<Product> products = new ArrayList<>();
        if (fakestoreProductDtos != null) {
            for (FakestoreProductDto fakestoreProductDto : fakestoreProductDtos) {
                products.add(FakestoreProductMapper.mapToProduct(fakestoreProductDto));
            }
        }
        return products;*/
        List<Product> products = new ArrayList<>();

        ResponseEntity<FakestoreProductDto[]> response = fakestoreAPIClient.getForEntity(
                "https://fakestoreapi.com/products",
                FakestoreProductDto[].class);

        if (response.hasBody() &&
                response.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            FakestoreProductDto[] fakestoreProductDtos = response.getBody();

            for (FakestoreProductDto fakestoreProductDto : fakestoreProductDtos) {
                products.add(FakestoreProductMapper.mapToProduct(fakestoreProductDto));
            }

            return products;

        }

        return null;


    }

    @Override
    public Product createProduct(Product product) {
        FakestoreProductDto fakestoreProductDto = FakestoreProductMapper.mapToFakestoreProductDto(product);

        fakestoreProductDto = webClient.post()
                .uri("https://fakestoreapi.com/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(fakestoreProductDto)
                .retrieve()
                .bodyToMono(FakestoreProductDto.class)
                .block();
        Product createdProduct = FakestoreProductMapper.mapToProduct(fakestoreProductDto);


        return createdProduct;

    }

    @Override
    public Product updateProduct(Product product, Long productId) {
        /*FakestoreProductDto fakestoreProductDto = FakestoreProductMapper.mapToFakestoreProductDto(product);

        ResponseEntity<FakestoreProductDto> fakestoreResponseEntity = this.putForEntity("https://fakestoreapi.com/products/{productId}",
                fakestoreProductDto,
                FakestoreProductDto.class,
                productId);
        if (fakestoreResponseEntity.getStatusCode().is2xxSuccessful() && fakestoreResponseEntity.hasBody()) {
            return FakestoreProductMapper.mapToProduct(fakestoreResponseEntity.getBody());
        }

        return null;*/

        /*
        Billion dollar question?
         */
        FakestoreProductDto fakestoreProductDto = FakestoreProductMapper.mapToFakestoreProductDto(product);
        Long id = productId;


        ResponseEntity<FakestoreProductDto> response = fakestoreAPIClient.putForEntity(
                "https://fakestoreapi.com/products/{id}",
                fakestoreProductDto,
                FakestoreProductDto.class,
                id
        );

        if (response.hasBody() &&
                response.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
//            FakestoreProductDto fakestoreProductDto1 = response.getBody();
//            return fakestoreProductDto1.from(fakestoreProductDto1);
            return product;
        }

        return null;


    }
}
