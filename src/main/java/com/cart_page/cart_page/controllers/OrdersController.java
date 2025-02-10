package com.cart_page.cart_page.controllers;

import com.cart_page.cart_page.daos.OrdersDao;
import com.cart_page.cart_page.entities.Orders;
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

    @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(ordersDao.findAll());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Orders> getOrderByOrderId(@PathVariable int orderId) {
        return ResponseEntity.ok(ordersDao.findByOrderId(orderId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Orders> getOrderByUserId(@PathVariable int userId){
        return ResponseEntity.ok(ordersDao.findByOrderId(userId));
    }

    @PostMapping
    public ResponseEntity<Orders> createProduct(@RequestBody Orders order) {
        Orders createdOrder = ordersDao.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orders> updateOrder(@PathVariable int id, @RequestBody Orders order) {
        Orders updatedOrder = ordersDao.update(id, order);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
        if (ordersDao.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
