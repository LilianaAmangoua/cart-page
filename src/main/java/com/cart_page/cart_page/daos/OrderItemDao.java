package com.cart_page.cart_page.daos;

import com.cart_page.cart_page.entities.OrderItem;
import com.cart_page.cart_page.exceptions.OrderItemNotFound;
import com.cart_page.cart_page.exceptions.ProductNotFound;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<OrderItem> orderItemRowMapper = (rs, _) -> new OrderItem(
            rs.getInt("orderId"),
            rs.getInt("productId"),
            rs.getInt("quantity")
    );

    public List<OrderItem> findAll() { // Returns all products bought by all users
        String sql = "SELECT * FROM order_item";
        return jdbcTemplate.query(sql, orderItemRowMapper);
    }

    public OrderItem findById(int productId, int orderId) { // Returns a product's quantity in an order
        String sql = "SELECT * FROM order_item WHERE productId = ? AND orderId = ?";
        return jdbcTemplate.query(sql, orderItemRowMapper, productId, orderId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new OrderItemNotFound("Order Item not found : Commande " + orderId + "avec le produit id : " + productId + " n'existe pas"));
    }

    public OrderItem findByOrderId(int orderId){ // Returns all products of an order
        String sql = "SELECT * FROM order_item WHERE orderId = ?";
        return  jdbcTemplate.query(sql, orderItemRowMapper, orderId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new OrderItemNotFound("Order Item not found : Commande avec l'id " + orderId + " n'existe pas"));
    }

    public OrderItem save(OrderItem orderItem) {
        String sql = "INSERT INTO order_item (orderId, productId, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity());

        return orderItem;
    }

    public OrderItem update(int productId, int orderId, OrderItem orderItem) {
        if (!orderItemExists(productId, orderId)) {
            throw new OrderItemNotFound("Order Item not found : Commande " + orderId + " avec le produit id : " + productId + " n'existe pas");
        }

        String sql = "UPDATE order_item SET orderId = ?, productId = ?, quantity = ? WHERE productId = ? AND orderId = ?";
        int rowsAffected = jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity(), productId, orderId);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour de la commande" + orderId + "avec le produit id : " + productId);
        }

        return this.findById(productId, orderId);
    }

    public boolean delete(int productId, int orderId) {
        String sql = "DELETE FROM order_item WHERE productId = ? AND orderId = ?";
        int rowsAffected = jdbcTemplate.update(sql, productId, orderId);
        return rowsAffected > 0;
    }

    private boolean orderItemExists(int productId, int orderId) {
        String checkSql = "SELECT COUNT(*) FROM order_item WHERE productId = ? AND orderId = ? ";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, productId, orderId);
        return count > 0;
    }
}
