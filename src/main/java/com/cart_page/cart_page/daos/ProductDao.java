package com.cart_page.cart_page.daos;

import com.cart_page.cart_page.entities.Product;
import com.cart_page.cart_page.exceptions.ProductNotFound;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDao {
    private final JdbcTemplate jdbcTemplate;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Product> productRowMapper = (rs, _) -> new Product(
            rs.getInt("productId"),
            rs.getString("name"),
            rs.getBigDecimal("price"),
            rs.getInt("stock"),
            rs.getString("product_description")
    );

    public List<Product> findAll() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    public Product findById(int id) {
        String sql = "SELECT * FROM product WHERE productId = ?";
        return jdbcTemplate.query(sql, productRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ProductNotFound("Product Not Found : Produit avec l'ID : " + id + " n'existe pas"));
    }

    public Product save(Product product) {
        String sql = "INSERT INTO product (name, price, stock, product_description) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getStock(), product.getDescription());

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        int id = jdbcTemplate.queryForObject(sqlGetId, Integer.class);

        product.setProductId(id);
        return product;
    }

    public Product update(int id, Product product) {
        if (!productExists(id)) {
            throw new ProductNotFound("Product Not Found : Produit avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE product SET name = ?, price = ?, stock = ?, product_description = ? WHERE productId = ?";
        int rowsAffected = jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getStock(), product.getDescription(), id);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour du produit avec l'ID : " + id);
        }

        return this.findById(id);
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM product WHERE productId = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    private boolean productExists(int id) {
        String checkSql = "SELECT COUNT(*) FROM product WHERE productId = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }
}
