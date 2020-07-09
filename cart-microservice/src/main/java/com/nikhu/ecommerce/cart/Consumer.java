package com.nikhu.ecommerce.cart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
import java.util.logging.Level;

@EnableBinding(Sink.class)
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

	@Autowired
    private CartRepository cartRepository;
	
    @StreamListener(target = Sink.INPUT)
    public void consume(String message){
        logger.info("Receive a String Message: "+message);
        try {
            Discount disc = new ObjectMapper().readValue(message, Discount.class);
            logger.info("Id Cart:  " + disc.getId());
            logger.info("Discount Amount: " + disc.getAmount());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
    @StreamListener(target = Sink.INPUT, condition = "headers['type']=='discount'")
    public void handle(@Payload Discount message){
        Cart cart = cartRepository.getCartById(message.getId());
        if (cart != null) {
                cart.insertDiscount(message.getAmount());
        }
        logger.info("Cart dengan ID " + message.getId() + " mendapat diskon sebesar Rp." + message.getAmount());
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
