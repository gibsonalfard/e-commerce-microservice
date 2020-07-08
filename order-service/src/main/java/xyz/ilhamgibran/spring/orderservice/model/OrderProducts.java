package xyz.ilhamgibran.spring.orderservice.model;

import javax.persistence.*;

@Entity
public class OrderProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderProductId;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private
    Orders orders;

    @ManyToOne
    @JoinColumn(name = "productId")
    private
    Products products;
    private int qty;

    public OrderProducts(Orders orders, Products prod, int qty){
        setOrders(orders);
        setProducts(prod);
        setQty(qty);
    }


    public int getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(int orderProductId) {
        this.orderProductId = orderProductId;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
