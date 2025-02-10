package com.cart_page.cart_page.exceptions;

public class OrderItemNotFound extends RuntimeException {
    public OrderItemNotFound(String message) {
        super(message);
    }
}
