package xyz.ilhamgibran.spring.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import xyz.ilhamgibran.spring.orderservice.model.Orders;
import xyz.ilhamgibran.spring.orderservice.model.Products;
import xyz.ilhamgibran.spring.orderservice.repository.OrdersRepository;
import xyz.ilhamgibran.spring.orderservice.repository.ProductsRepository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@EnableBinding(Sink.class)
public class ConsumerController {
    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    private static final Logger logger = LoggerFactory.getLogger(ConsumerController.class);
    private List<Products> productsList = new ArrayList<>();

    @StreamListener(target = Sink.INPUT)
    public void stringConsume(String message){
        try {
            Products prod = new ObjectMapper().readValue(message, Products.class);
            logger.info("This is ID of Data : " + prod.getId());
            productsRepository.save(prod);

            productsList.add(prod);
            productsList.add(prod);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            Date now = new Date(Calendar.getInstance().getTime().getTime());

            for(int i=0;i<productsList.size();i++){
                Orders orders = new Orders(now, productsList.get(i).getId());
                ordersRepository.save(orders);
            }

            productsList.clear();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
