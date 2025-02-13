package com.cart_page.cart_page.daos;

import com.cart_page.cart_page.entities.User;
import com.cart_page.cart_page.exceptions.UserNotFound;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, _) -> new User(
            rs.getInt("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("role")
    );

    public List<User> findAll(){
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        return jdbcTemplate.query(sql, userRowMapper, email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFound("Utilisateur non trouvé"));
    }

    public User findById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        return jdbcTemplate.query(sql, userRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFound("User avec l'ID : " + id + " n'existe pas"));
    }

    public List<Map<String, Object>> getUserEmails(){
        String sql = "SELECT user.email, orders.orderId, orders.total, orders.order_date\n" +
                "FROM user\n" +
                "INNER JOIN orders ON user.id = orders.userId\n" +
                "WHERE user.role = 'USER'";
        return jdbcTemplate.queryForList(sql);
    }

    public boolean save(User user) {
        String sql = "INSERT INTO user (email, password, role) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getRole());
        return rowsAffected > 0;
    }

    public User update(int id, User user) {
        if (!userExistsById(id)) {
            throw new UserNotFound("User avec l'ID : " + id + " n'existe pas");
        }

        String sql = "UPDATE user SET email = ?, password = ?, role = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,user.getEmail(), user.getPassword(), user.getRole(), id);

        if (rowsAffected <= 0) {
            throw new RuntimeException("Échec de la mise à jour de l'utilisateur avec l'ID : " + id);
        }
        return this.findById(id);
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }


    public boolean userExistsById(int id) {
        String checkSql = "SELECT COUNT(*) FROM user WHERE id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email) > 0;
    }
}
