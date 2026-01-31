package org.ruby.productcatalogservice.repositories;

import org.ruby.productcatalogservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    Optional<Product> findById(Long id);

    @Override
    List<Product> findAll();

    @Override
    Product save(Product product);

    List<Product> findProductsByPriceBetween(Double minPrice, Double maxPrice);

    /*
    Find all products order by price ascending
     */
    List<Product> findAllByOrderByPriceAsc();

    //Hibernate Query to get description where id is
    @Query("SELECT p.description FROM Product p WHERE p.id = :id")
    String findDescriptionWhereIdIs(@Param("id") Long id);

    /*
    Native Query to get all products where name like
     */
    @Query(value = "SELECT * FROM product p WHERE p.name LIKE %:name%", nativeQuery = true)
    List<Product> findAllProductsWhereNameLike(@Param("name") String name);

}
