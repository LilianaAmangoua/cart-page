package com.cart_page.cart_page.daos;

import com.cart_page.cart_page.entities.Order;
import com.cart_page.cart_page.exceptions.OrderNotFound;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrdersDao {
    private final JdbcTemplate jdbcTemplate;

    public OrdersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Order> ordersRowMapper = (rs, _) -> new Order(
            rs.getInt("orderId"),
            rs.getInt("userId"),
            rs.getBigDecimal("total"),
            rs.getDate("order_date")
    );

    public List<Order> findAll() { // Returns all orders of all users
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, ordersRowMapper);
    }

    public Order findByOrderId(int id) { // Returns an order with its total and the date of purchase
        String sql = "SELECT * FROM orders WHERE orderId = ?";
        return jdbcTemplate.query(sql, ordersRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new OrderNotFound("Order Not Found : Commande avec l'ID : " + id + " n'existe pas"));
    }


    public Order findByUserId(int id) { // Returns a user's orders
        String sql = "SELECT * FROM orders WHERE userId = ?";
        return jdbcTemplate.query(sql, ordersRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new OrderNotFound("Order Not Found : User avec l'ID : " + id + " n'existe pas"));
    }


    public Order save(Order newOrder) {
        String sql = "INSERT INTO orders (userId, total, order_date) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, newOrder.getUserId(), newOrder.getTotal(), newOrder.getOrder_date());

        return newOrder;
    }

    public List<Order> searchOrder(String query, int userId){
        String sql = "SELECT * FROM orders WHERE orderId LIKE ? AND userId = ? ";
        return jdbcTemplate.query(sql,ordersRowMapper, "%" + query + "%", userId);
    }

    public Order update(int orderId, Order order) {
        if (!orderExists(orderId)) {
            throw new OrderNotFound("Commande avec l'ID : " + orderId + " n'existe pas");
        }

        String sql = "UPDATE product SET userId = ?, total = ?,order_date = ? WHERE orderId = ?";
        int rowsAffected = jdbcTemplate.update(sql, order.getUserId(), order.getTotal(), order.getOrder_date(), orderId);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour de la commande avec l'ID : " + orderId);
        }

        return this.findByOrderId(orderId);
    }

    public boolean delete(int orderId) {
        String sql = "DELETE FROM orders WHERE orderId = ?";
        int rowsAffected = jdbcTemplate.update(sql, orderId);
        return rowsAffected > 0;
    }

    private boolean orderExists(int userId) {
        String checkSql = "SELECT COUNT(*) FROM orders WHERE orderId = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, userId);
        return count > 0;
    }
}
