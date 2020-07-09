package xyz.ilhamgibran.spring.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.messaging.handler.annotation.Payload;
import xyz.ilhamgibran.spring.orderservice.model.*;
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

    private Products convertToProduct(KafkaMessage km){
        Products prod = new Products();
        prod.setId(km.getId().getTimestamp());
        prod.setName(km.getName());
        prod.setUrl(km.getUrl());
        prod.setPrice(km.getPrice());
        prod.setImage(km.getImage());
        prod.setDescription(km.getDescription());
        prod.setCurrency(km.getCurrency());

        return prod;
    }

    private ObjectId setObjFromString(String message) throws JsonProcessingException {
        String msgSub = message.substring(7,70);
        ObjectId obj = new ObjectMapper().readValue(msgSub, ObjectId.class);
        System.out.println(obj == null ? "NULL lagi" : obj.getTimestamp());
        return obj;
    }

    // Method for Insert Message
    @StreamListener(target = Sink.INPUT, condition = "headers['type']=='product-insert'")
    public void consumeInsert(String message){
        try {
            KafkaMessage mes = new ObjectMapper().readValue(message, KafkaMessage.class);
//            {"_id":{"timestamp":1594329363,"date":"2020-07-09T21:16:03.000+00:00"}
            // Object ID cannot be mapped, so i separate ObjectId Mapping
            if(mes.getId() == null){
                mes.setId(setObjFromString(message));
            }
            System.out.println(mes.getId() == null ? "null" : mes.getId().getTimestamp());
            Products prod = this.convertToProduct(mes);

            productsRepository.save(prod);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Method for Update message
    @StreamListener(target = Sink.INPUT, condition = "headers['type']=='product-update'")
    public void consumeUpdate(String message){
        try {
            KafkaMessage prod = new ObjectMapper().readValue(message, KafkaMessage.class);
            if(prod.getId() == null){
                prod.setId(setObjFromString(message));
            }
            Products dbProd = productsRepository.getProductById(prod.getId().getTimestamp());

            if(dbProd != null){
                dbProd.setName(prod.getName());
                dbProd.setCurrency(prod.getCurrency());
                dbProd.setDescription(prod.getDescription());
                dbProd.setImage(prod.getImage());
                dbProd.setPrice(prod.getPrice());
                dbProd.setUrl(prod.getUrl());

                logger.info("This is Update of Data : " + dbProd.getId());
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
            KafkaMessage prod = new ObjectMapper().readValue(message, KafkaMessage.class);
            if(prod.getId() == null){
                prod.setId(setObjFromString(message));
            }
//            Products prod = new ObjectMapper().readValue(message, Products.class);
            logger.info("This Delete ID of Data : " + prod.getId().getTimestamp());
            productsRepository.deleteById(prod.getId().getTimestamp());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
