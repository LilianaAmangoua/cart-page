package com.cart_page.cart_page.controllers;

import com.cart_page.cart_page.daos.OrdersDao;
import com.cart_page.cart_page.entities.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersDao ordersDao;


    public OrdersController(OrdersDao ordersDao) {
        this.ordersDao = ordersDao;
    }

    /*@GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(ordersDao.findAll());
    }*/

    @GetMapping("/{orderId}") // Retourne une commande avec son total et la date
    public ResponseEntity<Order> getOrderByOrderId(@PathVariable int orderId) {
        return ResponseEntity.ok(ordersDao.findByOrderId(orderId));
    }

    @GetMapping("/{userId}") // Retourne toutes les commandes d'un utilisateur
    public ResponseEntity<Order> getOrderByUserId(@PathVariable int userId){
        return ResponseEntity.ok(ordersDao.findByUserId(userId));
    }

    @PostMapping // Ajoute une nouvelle commande d'un utilisateur
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = ordersDao.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/{orderId}") // Met à jour une commande d'un utilisateur
    public ResponseEntity<Order> updateOrder(@PathVariable int orderId, @RequestBody Order order) {
        Order updatedOrder = ordersDao.update(orderId, order);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{orderId}") // Supprime une commande d'un utilisateur
    public ResponseEntity<Void> deleteOrder(@PathVariable int orderId) {
        if (ordersDao.delete(orderId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
