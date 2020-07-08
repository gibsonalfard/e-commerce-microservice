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

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderProductsRepository orderProductsRepository;

    private static final Logger logger = LoggerFactory.getLogger(ConsumerController.class);
    private List<Products> productsList = new ArrayList<>();

    @StreamListener(target = Sink.INPUT)
    public void stringConsume(String message){
        try {
            double total = 0;
            Products prod = new ObjectMapper().readValue(message, Products.class);
            logger.info("This is ID of Data : " + prod.getId());
            productsRepository.save(prod);

            // Loop Add Product
            productsList.add(prod);
            total += prod.getPrice();

            productsList.add(prod);
            total += prod.getPrice();

            // Add Order
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            Date now = new Date(Calendar.getInstance().getTime().getTime());

            Orders orders = new Orders(now, total);
            ordersRepository.save(orders);

            // Add OrderProduct
            productsList.sort(Comparator.comparing(Products::getId));  // Sort  ProductList By ID
            int qty = 0;
            for(int i=0;i<productsList.size();i++){
                if(i != 0){
                    if(productsList.get(i).getId() != productsList.get(i-1).getId()){
                        orderProductsRepository
                                .save(new OrderProducts(orders, productsList.get(i-1), qty));
                        qty = 1;
                    }
                }
                qty += 1;
            }

            // Insert Last Product in List
            orderProductsRepository
                    .save(new OrderProducts(orders, productsList.get(productsList.size()-1), qty));

            productsList.clear();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
