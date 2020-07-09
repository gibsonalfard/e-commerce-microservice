package com.nikhu.ecommerce.cart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@EnableBinding(Sink.class)
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

	@Autowired
    private CartRepository cartRepository;
	
    @StreamListener(target = Sink.INPUT)
    public void consume(String message){
        logger.info("Receive a String Message: "+message);
		/*
		try {
            User usr = new ObjectMapper().readValue(message, User.class);
            logger.info("NIM: " + usr.getNim());
            logger.info("Nama: " + usr.getName());
            logger.info("SKS Selesai: " + usr.getSksTake());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		*/
    }
	
    @StreamListener(target = Sink.INPUT, condition = "headers['type']=='discount'")
    public void handle(@Payload Discount message){
        try {
            Discount disc = new ObjectMapper().readValue(message, Discount.class);
			Cart cart = cartRepository.getCartById(id);
			if (cart != null) {
				cart.insertDiscount(disc.getAmount());
			}
            logger.info("Cart dengan ID " + disc.getId() + " mendapat diskon sebesar Rp." + disc.getAmount());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
	/*

    @StreamListener(target = Sink.INPUT, condition = "headers['type']=='user'")
    public void handleUser(@Payload User usr){
        final DateTimeFormatter df = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                .withZone(ZoneId.systemDefault());
        logger.info("Receive a complex message: ");
        logger.info("NIM: " + usr.getNim());
        logger.info("Nama: " + usr.getName());
        logger.info("SKS Selesai: " + usr.getSksTake());
    }
	*/
}
