package com.cart_page.cart_page.controllers;

import com.cart_page.cart_page.daos.OrdersDao;
import com.cart_page.cart_page.entities.Order;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersDao ordersDao;


    public OrdersController(OrdersDao ordersDao) {
        this.ordersDao = ordersDao;
    }

    @GetMapping("/all") // Admin mapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(ordersDao.findAll());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderByOrderId(@PathVariable int orderId) {
        return ResponseEntity.ok(ordersDao.findByOrderId(orderId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Order> getOrderByUserId(@PathVariable int userId){
        return ResponseEntity.ok(ordersDao.findByUserId(userId));
    }

    @GetMapping("/{userId}/search")
    public ResponseEntity<List<Order>> searchProduct(@PathVariable int userId, @RequestParam String query){
        return ResponseEntity.ok(ordersDao.searchOrder(query, userId));
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
        Order createdOrder = ordersDao.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable int orderId, @RequestBody Order order) {
        Order updatedOrder = ordersDao.update(orderId, order);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int orderId) {
        if (ordersDao.delete(orderId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
