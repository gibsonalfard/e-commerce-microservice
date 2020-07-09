package xyz.ilhamgibran.spring.orderservice.model;

public class RequestOrder {
    private int productId;
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
