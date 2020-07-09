package xyz.ilhamgibran.spring.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.ilhamgibran.spring.orderservice.model.OrderProducts;
import xyz.ilhamgibran.spring.orderservice.model.Orders;
import xyz.ilhamgibran.spring.orderservice.model.Products;
import xyz.ilhamgibran.spring.orderservice.model.RequestOrder;
import xyz.ilhamgibran.spring.orderservice.repository.OrderProductsRepository;
import xyz.ilhamgibran.spring.orderservice.repository.OrdersRepository;
import xyz.ilhamgibran.spring.orderservice.repository.ProductsRepository;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

@RestController
public class OrdersController{
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderProductsRepository orderProductsRepository;

    @Autowired
    private ProductsRepository productsRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);
    private List<Products> productsList = new ArrayList<>();

    @PostMapping("/order")
    public String orderProduct(@RequestBody String json){
        double total = 0;
        try {
            List<RequestOrder> message = new ObjectMapper()
                    .readValue(json, new TypeReference<List<RequestOrder>>() {});

            logger.info("Number of Object : " + message.size());
            logger.info("Object 1 Id : " + message.get(0).getProductId());

            // Get Product List
            for(int i=0;i<message.size();i++){
                productsList.add(productsRepository
                        .getProductById(message.get(i).getProductId()));
                total += message.get(i).getQty()*productsList.get(i).getPrice();
            }
            // Add Order
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            Date now = new Date(Calendar.getInstance().getTime().getTime());

            Orders orders = new Orders(now, total);
            ordersRepository.save(orders);

            for (int i = 0; i < productsList.size(); i++){
                orderProductsRepository
                        .save(new OrderProducts(orders, productsList.get(i), message.get(i).getQty()));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "BolehLah";
    }

    public void addOrderProduct(Orders orders){
        // Add OrderProduct
        productsList.sort(Comparator.comparing(Products::getId));  // Sort  ProductList By ID
        int qty = 0;
        for(int i=0;i<productsList.size();i++){
            if(i != 0){
                if(productsList.get(i).getId() != productsList.get(i-1).getId()){
                    orderProductsRepository
                            .save(new OrderProducts(orders, productsList.get(i-1), qty));
                    qty = 0;
                }
            }
            qty += 1;
        }

        // Insert Last Product in List
        orderProductsRepository
                .save(new OrderProducts(orders, productsList.get(productsList.size()-1), qty));

        productsList.clear();
    }
}
