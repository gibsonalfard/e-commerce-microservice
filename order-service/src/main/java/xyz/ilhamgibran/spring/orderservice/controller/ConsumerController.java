package xyz.ilhamgibran.spring.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import xyz.ilhamgibran.spring.orderservice.model.OrderProducts;
import xyz.ilhamgibran.spring.orderservice.model.Orders;
import xyz.ilhamgibran.spring.orderservice.model.Products;
import xyz.ilhamgibran.spring.orderservice.repository.OrderProductsRepository;
import xyz.ilhamgibran.spring.orderservice.repository.OrdersRepository;
import xyz.ilhamgibran.spring.orderservice.repository.ProductsRepository;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.*;

@EnableBinding(Sink.class)
public class ConsumerController {
    @Autowired
    private ProductsRepository productsRepository;

    private static final Logger logger = LoggerFactory.getLogger(ConsumerController.class);

    @StreamListener(target = Sink.INPUT)
    public void stringConsume(String message){
        try {
            double total = 0;
            Products prod = new ObjectMapper().readValue(message, Products.class);
            logger.info("This is ID of Data : " + prod.getId());
            productsRepository.save(prod);

//            // Loop Add Product
//            productsList.add(prod);
//            total += prod.getPrice();
//
//            productsList.add(prod);
//            total += prod.getPrice();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
