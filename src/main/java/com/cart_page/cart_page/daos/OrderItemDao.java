package com.cart_page.cart_page.daos;

import com.cart_page.cart_page.entities.OrderItem;
import com.cart_page.cart_page.exceptions.OrderItemNotFound;
import com.cart_page.cart_page.exceptions.ProductNotFound;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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

    public List<OrderItem> findAll() {
        String sql = "SELECT * FROM order_item";
        return jdbcTemplate.query(sql, orderItemRowMapper);
    }

    public OrderItem findById(int productId, int orderId) {
        String sql = "SELECT * FROM order_item WHERE productId = ? AND orderId = ?";
        return jdbcTemplate.query(sql, orderItemRowMapper, productId, orderId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new OrderItemNotFound("Order Item not found : Commande " + orderId + "avec le produit id : " + productId + " n'existe pas"));
    }

    public OrderItem save(OrderItem orderItem) {
        String sql = "INSERT INTO order_item (orderId, productId, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        orderItem.setProductId(id);
        return orderItem;
    }

    public OrderItem update(int productId, int orderId, OrderItem orderItem) {
        if (!orderItemExists(productId, orderId)) {
            throw new OrderItemNotFound("Order Item not found : Commande " + orderId + " avec le produit id : " + productId + " n'existe pas");
        }

        String sql = "UPDATE order_item SET orderId = ?, productId = ?, quantity = ? WHERE productId = ?";
        int rowsAffected = jdbcTemplate.update(sql, orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity(), productId);

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
