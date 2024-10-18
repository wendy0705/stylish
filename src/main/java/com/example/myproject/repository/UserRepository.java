package com.example.myproject.repository;

import com.example.myproject.dto.UserDTO;
import com.example.myproject.exception.PasswordIsWrongException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
        return count != null && count > 0;
    }

    public void save(UserDTO user) {
        String sql = "INSERT INTO user (provider, name, email, picture, password) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
//        log.info("dao:" + user.getPassword());
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getProvider());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPicture());
            ps.setString(5, passwordEncoder.encode(user.getPassword()));
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
    }

    public boolean checkPassword(UserDTO user) {

        String sql = "SELECT * FROM user WHERE email = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, user.getEmail());

        String storedPassword = (String) result.get("password");

        if (storedPassword == null || !passwordEncoder.matches(user.getPassword(), storedPassword)) {
            throw new PasswordIsWrongException("Password is incorrect.");
        }
        String name = (String) result.get("name");
        Long userId = ((Number) result.get("id")).longValue();
        user.setName(name);
        user.setId(userId);
        return true;
    }

    public void setUserId(UserDTO user) {
        String sql = "SELECT id FROM user WHERE email = ?";
        Long userId = jdbcTemplate.queryForObject(sql, new Object[]{user.getEmail()}, Long.class);
        user.setId(userId);
    }

    public void updateUserProvider(String email, String provider) {
        String sql = "UPDATE user SET provider = ? WHERE email = ?";
        jdbcTemplate.update(sql, provider, email);
    }

    public Map<String, Object> getProfile(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";

        Map<String, Object> userProfile = jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) -> {
            Map<String, Object> data = new HashMap<>();
            data.put("provider", rs.getString("provider"));
            data.put("name", rs.getString("name"));
            data.put("email", rs.getString("email"));
            String picture = rs.getString("picture");
            data.put("picture", picture != null ? picture : "null");
            return data;
        });

        return userProfile;
    }

}
