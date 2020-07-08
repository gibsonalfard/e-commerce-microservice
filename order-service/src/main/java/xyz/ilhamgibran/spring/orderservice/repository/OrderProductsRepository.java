package xyz.ilhamgibran.spring.orderservice.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.ilhamgibran.spring.orderservice.model.OrderProducts;

public interface OrderProductsRepository extends CrudRepository<OrderProducts, String> {
}
