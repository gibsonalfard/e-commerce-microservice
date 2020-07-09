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

    // Method for Insert Message
    @StreamListener(target = Sink.INPUT, condition = "headers['type']=='product-insert'")
    public void consumeInsert(String message){
        try {
            double total = 0;
            Products prod = new ObjectMapper().readValue(message, Products.class);
            logger.info("This is ID of Data : " + prod.getId());
            productsRepository.save(prod);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Method for Update message
    @StreamListener(target = Sink.INPUT, condition = "headers['type']=='product-update'")
    public void consumeUpdate(String message){
        try {
            Products prod = new ObjectMapper().readValue(message, Products.class);
            logger.info("This Update ID of Data : " + prod.getId());
            Products dbProd = productsRepository.getProductById(prod.getId());

            if(dbProd != null){
                dbProd.setName(prod.getName());
                dbProd.setCurrency(prod.getCurrency());
                dbProd.setDescription(prod.getDescription());
                dbProd.setImage(prod.getImage());
                dbProd.setPrice(prod.getPrice());
                dbProd.setUrl(prod.getUrl());

                productsRepository.save(dbProd);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Method for Delete message
    @StreamListener(target = Sink.INPUT, condition = "headers['type']=='product-delete'")
    public void consumeDelete(String message){
        try {
            Products prod = new ObjectMapper().readValue(message, Products.class);
            logger.info("This Delete ID of Data : " + prod.getId());
            productsRepository.deleteById(prod.getId());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
