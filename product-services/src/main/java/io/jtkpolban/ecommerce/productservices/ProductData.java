package io.jtkpolban.ecommerce.productservices;

import org.bson.types.ObjectId;

public class ProductData {
    private String _id;
    private String name;
    private String description;
    private float price;
    private String currency;
    private String image;
    private String url;

    public ProductData() {}

    public ProductData(Product productMongoDB) {
        this._id = productMongoDB.get_id().toHexString();
        this.name = productMongoDB.getName();
        this.description = productMongoDB.getDescription();
        this.price = productMongoDB.getPrice();
        this.currency = productMongoDB.getCurrency();
        this.image = productMongoDB.getImage();
        this.url = productMongoDB.getUrl();
    }

    public ProductData getProductData(Product productMongoDB) {
        return new ProductData(productMongoDB);
    }
}
