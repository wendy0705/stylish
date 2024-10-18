package com.example.myproject.repository;

import com.example.myproject.dto.OrderDTO;
import com.example.myproject.dto.PaymentDTO;
import com.example.myproject.dto.ProductOrderDTO;
import com.example.myproject.dto.RecipientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class OrderRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long saveOrder(OrderDTO order) {

        String sql = "INSERT INTO orders (shipping, payment, subtotal, freight, total) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, order.getShipping());
            ps.setString(2, order.getPayment());
            ps.setLong(3, order.getSubtotal());
            ps.setLong(4, order.getFreight());
            ps.setLong(5, order.getTotal());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return id;

    }

    public void savePayment(PaymentDTO payment) {
        String sql = "INSERT INTO payment (successful, order_id) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setBoolean(1, payment.isSuccessful());
            ps.setLong(2, payment.getOrderId());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            payment.setId(keyHolder.getKey().longValue());
        }
    }

    public void saveOrderList(List<ProductOrderDTO> items, Long orderId) {
        String sql = "INSERT INTO product_order (product_id, order_id, quantity) VALUES (?, ?, ?)";

        for (ProductOrderDTO item : items) {
            jdbcTemplate.update(sql, item.getProductId(), orderId, item.getQuantity());
        }
    }

    public void saveRecipient(RecipientDTO recipient) {
        String sql = "INSERT INTO recipient (name, phone, email, address, order_id, time) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, recipient.getName());
            ps.setString(2, recipient.getPhone());
            ps.setString(3, recipient.getEmail());
            ps.setString(4, recipient.getAddress());
            ps.setLong(5, recipient.getOrderId());
            ps.setString(6, recipient.getTime()); // Ensure correct format
            return ps;
        }, keyHolder);

        recipient.setId(keyHolder.getKey().longValue());
    }

}
