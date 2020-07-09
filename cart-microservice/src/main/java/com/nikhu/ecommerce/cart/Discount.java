package com.nikhu.ecommerce.cart;

public class Discount {
    private String id;
    private float amount;

    public Discount(){
        super();
    }

    public Discount(String id, float amount){
        this.setId(id);
        this.setAmount(amount);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}