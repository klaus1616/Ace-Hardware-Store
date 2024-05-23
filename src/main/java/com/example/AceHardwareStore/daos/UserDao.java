package com.example.AceHardwareStore.daos;


import com.example.AceHardwareStore.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * User data access object
 */
@Component
public class UserDao {
    /**
     * JDBC template
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * Password encoder
     */
    private PasswordEncoder passwordEncoder;

    /**
     * Constructor
     *
     * @param dataSource data source
     * @param passwordEncoder password encoder
     */
    public UserDao(DataSource dataSource, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get all users
     *
     * @return list of users
     */
    public List<User> getAllUsers() {
        return jdbcTemplate.query(
                "SELECT * FROM users order by username",
                this::mapRowToUser
        );
    }

    /**
     * Get user by username
     *
     * @param username username
     * @return user
     */
    public User getUser(String username) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM users WHERE username = ?",
                    this::mapRowToUser, username
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Create user
     *
     * @param user user
     * @return created user
     */
    public User createUser(User user) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO users (username, password, phone_number) VALUES (?, ?, ?)",
                    user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getPhoneNumber()
            );
            return getUser(user.getUsername());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Update user
     *
     * @param user user
     * @param updatePassword update password
     * @return updated user
     */
    public User updateUser(User user, boolean updatePassword) {
        if (updatePassword) {
            jdbcTemplate.update(
                    "UPDATE users SET password = ?, phone_number = ? WHERE username = ?",
                    passwordEncoder.encode(user.getPassword()), user.getPhoneNumber(), user.getUsername()
            );
        } else {
            jdbcTemplate.update(
                    "UPDATE users SET phone_number = ? WHERE username = ?",
                    user.getPhoneNumber(), user.getUsername()
            );
        }
        return getUser(user.getUsername());
    }

    /**
     * Delete user
     *
     * @param username username
     */
    public void deleteUser(String username) {
        jdbcTemplate.update(
                "DELETE FROM users WHERE username = ?",
                username
        );
    }

    /**
     * Get roles for user
     *
     * @param username
     * @return
     */
    public List<String> getRolesForUser(String username) {
        return jdbcTemplate.queryForList(
                "SELECT role FROM roles WHERE username = ?",
                String.class, username
        );
    }

    /**
     * Add role to user
     *
     * @param username
     * @param role
     */
    public void addRoleToUser(String username, String role) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO roles (username, role) VALUES (?, ?)",
                    username, role
            );
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * Remove role from user
     *
     * @param username
     * @param role
     */
    public void removeRoleFromUser(String username, String role) {
        jdbcTemplate.update(
                "DELETE FROM roles WHERE username = ? AND role = ?",
                username, role
        );
    }

    /**
     * Check username and password
     *
     * @param username
     * @param password
     * @return
     */
    public boolean checkUsernamePassword(String username, String password) {
        User user = getUser(username);
        return passwordEncoder.matches(password, user.getPassword());
    }

    /**
     * Map row to user
     *
     * @param row row
     * @param rowNum row number
     * @return user
     * @throws SQLException
     */
    private User mapRowToUser(ResultSet row, int rowNum) throws SQLException {
        User user = new User();
        user.setUsername(row.getString("username"));
        user.setPassword(row.getString("password"));
        user.setPhoneNumber(row.getString("phone_number"));
        return user;
    }
}
