package com.cart_page.cart_page.controllers;

import com.cart_page.cart_page.daos.ProductDao;
import com.cart_page.cart_page.entities.Product;
import com.cart_page.cart_page.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductDao productDao;
    private final ProductService productService;

    public ProductController(ProductDao productDao, ProductService productService) {
        this.productDao = productDao;
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        return ResponseEntity.ok(productDao.findById(id));
    }

    @PatchMapping("/{id}/decrease")
    public ResponseEntity<?> decreaseStock(@PathVariable int id, @RequestParam int quantity){
        Product product = productDao.findById(id);
        boolean doesExists = productDao.productExists(product.getProductId());
        if(!doesExists){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produit avec l'id " + id + " non trouvé");
        }

        if(!productService.isStockSufficient(product)){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Stock épuisé");
        }

        Product newStockProduct = productService.decreaseStock(product, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(newStockProduct);

    }

    @PatchMapping("/{id}/increase")
    public ResponseEntity<?> increaseStock(@PathVariable int id, @RequestParam int quantity){
        Product product = productDao.findById(id);
        boolean doesExists = productDao.productExists(product.getProductId());
        if(!doesExists){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produit avec l'id " + id + " non trouvé");
        }

        if(!productService.isStockSufficient(product)){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Stock épuisé");
        }

        Product newStockProduct = productService.increaseStock(product, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(newStockProduct);

    }


    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String query){
        return ResponseEntity.ok(productDao.searchProduct(query));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productDao.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product product) {
        Product updatedProduct = productDao.update(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        if (productDao.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
