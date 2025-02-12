package com.cart_page.cart_page.controllers;

import com.cart_page.cart_page.daos.OrderItemDao;
import com.cart_page.cart_page.entities.OrderItem;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderItem")
public class OrderItemController {
    private final OrderItemDao orderItemDao;

    public OrderItemController(OrderItemDao orderItemDao) {
        this.orderItemDao = orderItemDao;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderItem> getAllProductsFromAnOrder(@PathVariable int orderId){
        return ResponseEntity.ok(orderItemDao.findByOrderId(orderId));
    }

    @GetMapping("/{productId}/{orderId}")
    public ResponseEntity<OrderItem> getOrderByOrderItemId(@PathVariable int productId, int orderId) {
        return ResponseEntity.ok(orderItemDao.findById(productId, orderId));
    }

    @PostMapping
    public ResponseEntity<OrderItem> createOrderItem(@Valid @RequestBody OrderItem orderItem) {
        OrderItem createdOrderItem = orderItemDao.save(orderItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderItem);
    }

    @PutMapping("/{productId}/{orderId}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable int productId, int orderId,  @RequestBody OrderItem orderItem) {
        OrderItem updatedOrderItem = orderItemDao.update(productId,orderId, orderItem);
        return ResponseEntity.ok(updatedOrderItem);
    }

    @DeleteMapping("/{productId}/{orderId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable int productId, int orderId) {
        if (orderItemDao.delete(productId, orderId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
