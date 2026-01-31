package org.ruby.productcatalogservice.services;

import org.ruby.productcatalogservice.models.Product;
import org.ruby.productcatalogservice.models.State;
import org.ruby.productcatalogservice.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StorageProductService implements IProductService{
    private final ProductRepository productRepository;
    public StorageProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public Product getProductById(Long id) {
        Optional<Product> optionalProduct=productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    @Override
    public List<Product> getProducts() {
        return  productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {
        Optional<Product> optionalProduct=productRepository.findById(product.getId());
        if(optionalProduct.isPresent()){
            // Product with the same ID already exists and we are throwing exception
            return null;
        }
        return productRepository.save(product);
    }

    //PUT method to replace the entire product
    @Override
    public Product replaceProduct(Product product, Long id) {
        // Validate that product has at least one non-null field
        if (product == null || isProductEmpty(product)) {
            throw new IllegalArgumentException("Product data cannot be empty");
        }

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();
            product.setId(id);
            product.setCreatedAt(existingProduct.getCreatedAt());
            return productRepository.save(product);
        }
        return null;
    }

    private boolean isProductEmpty(Product product) {
        return product.getName() == null &&
                product.getDescription() == null &&
                product.getPrice() == null &&
                product.getCategory() == null &&
                product.getImageUrl() == null;
    }

    public Boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .filter(product -> product.getState() != State.INACTIVE)
                .map(product -> {
                    product.setState(State.INACTIVE);
                    productRepository.save(product);
                    return true;
                })
                .orElse(false);
    }

    //TODO: Wherever  fetching products, we should make sure they are active


}
