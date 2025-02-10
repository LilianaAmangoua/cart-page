package com.cart_page.cart_page.controllers;

import com.cart_page.cart_page.daos.OrderItemDao;
import com.cart_page.cart_page.daos.OrdersDao;
import com.cart_page.cart_page.entities.OrderItem;
import com.cart_page.cart_page.entities.Orders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class OrderItemController {
    private final OrderItemDao orderItemDao;

    public OrderItemController(OrderItemDao orderItemDao) {
        this.orderItemDao = orderItemDao;
    }

    @GetMapping
    public ResponseEntity<List<OrderItem>> getAllOrdersItem() {
        return ResponseEntity.ok(orderItemDao.findAll());
    }

    @GetMapping("/{productId}/{orderId}")
    public ResponseEntity<OrderItem> getOrderByOrderItemId(@PathVariable int productId, int orderId) {
        return ResponseEntity.ok(orderItemDao.findById(productId, orderId));
    }

    @PostMapping
    public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItem orderItem) {
        OrderItem createdOrderItem = orderItemDao.save(orderItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable int productId, int orderId,  @RequestBody OrderItem orderItem) {
        OrderItem updatedOrderItem = orderItemDao.update(productId,orderId, orderItem);
        return ResponseEntity.ok(updatedOrderItem);
    }

    @DeleteMapping("/{productId}/{userId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable int productId, int userId) {
        if (orderItemDao.delete(productId, userId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
