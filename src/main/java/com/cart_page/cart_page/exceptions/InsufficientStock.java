package com.cart_page.cart_page.exceptions;

public class InsufficientStock extends RuntimeException {
    public InsufficientStock(String message) {
        super(message);
    }
}
