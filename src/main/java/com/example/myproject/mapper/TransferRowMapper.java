package com.example.myproject.mapper;

import com.example.myproject.dto.TransferBackDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TransferRowMapper implements RowMapper<TransferBackDTO> {

    @Value("${s3.host}")
    private String imageHost;

    private Map<Long, TransferBackDTO> backMap = new HashMap<>();

    @Override
    public TransferBackDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");

        TransferBackDTO backdata = backMap.get(id);

        if (backdata == null) {
            backdata = new TransferBackDTO();
            backdata.setId(rs.getLong("id"));
            backdata.setCategory(rs.getString("category"));
            backdata.setTitle(rs.getString("title"));
            backdata.setDescription(rs.getString("description"));
            backdata.setPrice(rs.getLong("price"));
            backdata.setTexture(rs.getString("texture"));
            backdata.setWash(rs.getString("wash"));
            backdata.setPlace(rs.getString("place"));
            backdata.setNote(rs.getString("note"));
            backdata.setStory(rs.getString("story"));
            backdata.setMain_image(imageHost + rs.getString("main_image"));

            backMap.put(id, backdata);
        }

        return backdata;
    }

}

