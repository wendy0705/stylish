package com.example.myproject.repository;

import com.example.myproject.dto.CampaignDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CampaignRepository {
    @Value("${s3.host}")
    private String imageHost;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(CampaignDTO campaign) {
        String sql = "INSERT INTO campaign(product_id, story, picture) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
//        log.info("dao:" + user.getPassword());
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, campaign.getProductId());
            ps.setString(2, campaign.getStory());
            ps.setString(3, campaign.getPicture());
            return ps;
        }, keyHolder);

        campaign.setId(keyHolder.getKey().longValue());
    }

    public List<CampaignDTO> getAllCampaigns() {
        String sql = "SELECT * FROM campaign";
        return jdbcTemplate.query(sql, new RowMapper<CampaignDTO>() {
            @Override
            public CampaignDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                CampaignDTO campaignDTO = new CampaignDTO();
                campaignDTO.setId(rs.getLong("id"));
                campaignDTO.setStory(rs.getString("story"));
                campaignDTO.setPicture(imageHost + rs.getString("picture"));
                campaignDTO.setProductId(rs.getLong("product_id"));
                return campaignDTO;
            }
        });
    }
}
