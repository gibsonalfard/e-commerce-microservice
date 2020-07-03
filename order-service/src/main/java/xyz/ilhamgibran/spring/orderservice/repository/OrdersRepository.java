package xyz.ilhamgibran.spring.orderservice.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.ilhamgibran.spring.orderservice.model.Orders;

public interface OrdersRepository extends CrudRepository<Orders, String> {
}
