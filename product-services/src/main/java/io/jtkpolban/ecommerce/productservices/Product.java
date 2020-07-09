package io.jtkpolban.ecommerce.productservices;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter
@Data
@Document(collection = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private ObjectId _id;
    private String name;
    private String description;
    private float price;
    private String currency;
    private String image;
    private String url;


//    @Field("published") //gunakan untuk different field name
//    private LocalDate publicationDate;

    public void set_id(ObjectId id) {
        _id = id;
    }

    public ObjectId get_id() { return this._id; }

    public String getName() { return this.name; }

    public String getDescription() { return this.description; }

    public float getPrice() { return this.price; }

    public String getCurrency() { return this.currency; }

    public String getImage() { return this.image; }

    public String getUrl() { return this.url; }

    public String get_stringId() { return this._id.toHexString(); }

    @Override
    public String toString() {
        return "Product [id = " + _id.toHexString() + " name = " + name + "]";
    }

}
