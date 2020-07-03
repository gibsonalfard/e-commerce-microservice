package xyz.ilhamgibran.spring.orderservice.controller;

import org.springframework.web.bind.annotation.RestController;
import xyz.ilhamgibran.spring.orderservice.model.Products;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OrdersController{
    public static List<Products> productsList = new ArrayList<>();

    public static List<Products> getProductsList() {
        return productsList;
    }
}
