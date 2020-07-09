package xyz.ilhamgibran.spring.orderservice.model;

public class RequestOrder {
    private Long productId;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
