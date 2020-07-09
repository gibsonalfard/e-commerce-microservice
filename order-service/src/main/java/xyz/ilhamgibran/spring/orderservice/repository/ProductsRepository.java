package xyz.ilhamgibran.spring.orderservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import xyz.ilhamgibran.spring.orderservice.model.Products;

public interface ProductsRepository extends CrudRepository<Products, Integer> {
    @Query(value="SELECT * FROM products WHERE id = ?1", nativeQuery = true)
    public Products getProductById(int id);
}
