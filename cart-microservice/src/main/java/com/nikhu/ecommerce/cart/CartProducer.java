package com.nikhu.ecommerce.cart;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@EnableBinding(Source.class)
public class CartProducer {
    private Source src;

    public CartProducer(Source src){
        super();
        this.setSource(src);
    }

    public Source getSource(){
        return this.src;
    }

    public void setSource(Source src){
        this.src = src;
    }
}
