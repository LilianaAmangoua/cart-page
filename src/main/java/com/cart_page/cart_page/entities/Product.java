package com.cart_page.cart_page.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public class Product {
    private int productId;

    @NotBlank(message = "Le nom du produit ne peut pas être vide.")
    private String name;

    @NotNull(message = "Le prix ne peut pas être null.")
    @Positive(message = "Le prix doit être positif.")
    private BigDecimal price;

    @NotNull(message = "Le stock ne peut pas être null.")
    @PositiveOrZero(message = "Le stock doit être positif ou égal à 0.")
    private int stock;

    private String description;

    @URL(message = "Le lien doit être valide")
    private String image;

    public Product(){

    }

    public Product(int productId, String name, BigDecimal price, int stock, String description, String image) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.image = image;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
