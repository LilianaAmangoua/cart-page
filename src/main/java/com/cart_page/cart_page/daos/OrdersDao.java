package com.cart_page.cart_page.daos;

import com.cart_page.cart_page.entities.Orders;
import com.cart_page.cart_page.entities.Product;
import com.cart_page.cart_page.exceptions.OrderNotFound;
import com.cart_page.cart_page.exceptions.ProductNotFound;
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

    private final RowMapper<Orders> ordersRowMapper = (rs, _) -> new Orders(
            rs.getInt("orderId"),
            rs.getInt("userId"),
            rs.getBigDecimal("total"),
            rs.getDate("order_date")
    );

    public List<Orders> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, ordersRowMapper);
    }

    public Orders findByOrderId(int id) {
        String sql = "SELECT * FROM orders WHERE orderId = ?";
        return jdbcTemplate.query(sql, ordersRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new OrderNotFound("Order Not Found : Commande avec l'ID : " + id + " n'existe pas"));
    }

    public Orders findByUserId(int id) {
        String sql = "SELECT * FROM orders WHERE userId = ?";
        return jdbcTemplate.query(sql, ordersRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new OrderNotFound("Order Not Found : User avec l'ID : " + id + " n'existe pas"));
    }

    public Orders save(Orders order) {
        String sql = "INSERT INTO orders (orderId, userId, total, order_date) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, order.getOrderId(), order.getUserId(), order.getTotal(), order.getOrder_date());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        order.setUserId(id);
        return order;
    }

    public Orders update(int orderId, Orders order) {
        if (!orderExists(orderId)) {
            throw new OrderNotFound("Commande avec l'ID : " + orderId + " n'existe pas");
        }

        String sql = "UPDATE product SET orderId = ?, userId = ?, total = ?,order_date = ? WHERE orderId = ?";
        int rowsAffected = jdbcTemplate.update(sql, order.getOrderId(), order.getUserId(), order.getTotal(), order.getOrder_date(), orderId);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour de la commande avec l'ID : " + orderId);
        }

        return this.findByOrderId(orderId);
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM orders WHERE orderId = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    private boolean orderExists(int userId) {
        String checkSql = "SELECT COUNT(*) FROM orders WHERE orderId = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, userId);
        return count > 0;
    }
}
