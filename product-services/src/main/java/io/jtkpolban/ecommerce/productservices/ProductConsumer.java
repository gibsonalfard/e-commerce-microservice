package io.jtkpolban.ecommerce.productservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
public class ProductConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ProductConsumer.class);
    @Autowired
    private final ProductRepository productRepository;

    public ProductConsumer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @StreamListener(target = Sink.INPUT)
    public void consume(String message){
        logger.info("Receive a String Message: "+message);
        try {
            Product pd = new ObjectMapper().readValue(message, Product.class);
            logger.info("ID: " + pd.get_id());
            logger.info("Stock: " + pd.getStock());
            this.productRepository.save(pd);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
