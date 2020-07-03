package xyz.ilhamgibran.spring.orderservice.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.ilhamgibran.spring.orderservice.model.Products;

public interface ProductsRepository extends CrudRepository<Products, String> {
}
