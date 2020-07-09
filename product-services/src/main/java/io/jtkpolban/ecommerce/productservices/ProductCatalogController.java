package io.jtkpolban.ecommerce.productservices;

import org.bson.types.ObjectId;
import org.omg.CORBA.Object;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class ProductCatalogController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCatalogController.class);

    private ProductProducer producer;

    @Autowired
    private final ProductRepository productRepository;

    public ProductCatalogController(ProductRepository productRepository, ProductProducer producer) {
        super();
        this.productRepository = productRepository;
        this.producer = producer;
    }

    @RequestMapping("/")
    public @ResponseBody
    String index() {
        return "<h1>Welcome to Product Catalog API!</h1><br/>use //products//list to list";
    }

    //ambil semua
    @GetMapping("/products/list")
    public @ResponseBody
    Flux<Product> productList() {
        return this.productRepository.findAll();
    }

    //ambil satu by id
    @GetMapping("/products/")
    public @ResponseBody
    Mono<Product> getOneProductById(@RequestParam String id) {
        ObjectId oid = new ObjectId(id);
        return this.productRepository.findById(oid);
    }

//    // tambah, need product, id kosongkan!
//    @PostMapping("/products/")
//    public @ResponseBody
//    Mono<Void> insertProduct(@RequestBody Product product){
//        return this.productRepository.save(product).then();
//    }

    // tambah, need product, id kosongkan!
    // lalu kirim ke message broker
    @PostMapping("/products/")
    public @ResponseBody
    String insertProduct(@RequestBody Product newProduct){
        ProductData productData = new ProductData();
        this.productRepository.save(newProduct)
        .subscribe(productData::setProductData);

        producer.getSource()
                .output()
                .send(MessageBuilder.withPayload(productData)
                        .setHeader("type", "product-insert")
                        .build());

        return "New Product Detail: " + newProduct.toString() ;
    }

//    // hapus, by id
//    @DeleteMapping("/products")
//    public @ResponseBody
//    Mono<Void> deleteProduct(@RequestParam String id){
//        ObjectId oid = new ObjectId(id);
//        return this.productRepository.deleteById(oid);
//    }

    // hapus, by id
    // kirimkan product yang di delete nya
    @DeleteMapping("/products")
    public @ResponseBody
    String deleteProduct(@RequestParam String id){
        ObjectId oid = new ObjectId(id);
        this.productRepository.findById(oid)
                .subscribe(product ->
                        producer.getSource()
                                .output()
                                .send(MessageBuilder.withPayload(product)
                                        .setHeader("type", "product-delete")
                                        .build()));

        this.productRepository.deleteById(oid);
        return "Product with ID: " + oid.toString() + " has been successfully deleted";
    }

//    //update product, by id, product body kosongkan id!
//    @PutMapping("/products")
//    public @ResponseBody
//    Mono<Void> updateProduct(@RequestParam String id, @RequestBody Product product){
//        product.set_id(new ObjectId(id));
//        return this.productRepository.save(product).then();
//    }

    //update product, by id, product body kosongkan id!
    //lalu kirim ke message broker
    @PutMapping("/products")
    public @ResponseBody
    String updateProduct(@RequestParam String id, @RequestBody Product updatedProduct){
        updatedProduct.set_id(new ObjectId(id));
        this.productRepository.save(updatedProduct)
                .subscribe(product ->
                        producer.getSource()
                                .output()
                                .send(MessageBuilder.withPayload(product)
                                        .setHeader("type", "product-update")
                                        .build()));

        return "Updated Product Detail: " + updatedProduct.toString() ;
    }

//    @PostMapping("/products/insert")
//    public String publishInsertedProduct(@RequestBody Product newProduct) {
//        this.productRepository.save(newProduct)
//                .subscribe(product ->
//                        producer.getSource()
//                                .output()
//                                .send(MessageBuilder.withPayload(product)
//                                        .setHeader("type", "insert-product")
//                                        .build()));
//
//        return newProduct.toString() + "has been successfully inserted";
//    }



    //default rest error handling
//    @ExceptionHandler(Exception.class)
//    void handleExceptions(HttpServletResponse response) throws IOException {
//        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error. We will be addressing this issue soon.");
//    }
}
