package xyz.ilhamgibran.spring.orderservice.model;

public class RequestOrder {
    private String productId;
    private int qty;

    public RequestOrder(){
        super();
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
