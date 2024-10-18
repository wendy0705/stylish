package com.example.myproject.repository;

import com.example.myproject.dto.*;
import com.example.myproject.mapper.TransferRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@Slf4j
public class ProductRepository {

    @Value("${storage.location}")
    private String storageLocation;


    @Value("${s3.host}")
    private String imageHost;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TransferRowMapper transferRowMapper;

    public void addProduct(ProductDTO product) {
        String sql = "INSERT INTO product (category, title, description, price, texture, wash, place, note, story, main_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getCategory());
            ps.setString(2, product.getTitle());
            ps.setString(3, product.getDescription());
            ps.setLong(4, product.getPrice());
            ps.setString(5, product.getTexture());
            ps.setString(6, product.getWash());
            ps.setString(7, product.getPlace());
            ps.setString(8, product.getNote());
            ps.setString(9, product.getStory());
            ps.setString(10, product.getMainImage());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            product.setId(keyHolder.getKey().longValue());
        }
    }

    public void addVariant(ProductDTO product, VariantDTO variant) {
        String sql = "INSERT INTO variant (product_id, stock) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, product.getId());
            ps.setInt(2, variant.getStock());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            variant.setId(keyHolder.getKey().longValue());
        }
    }

    public void addSizes(ProductDTO product, List<SizeDTO> sizes, VariantDTO variant) {
        for (SizeDTO size : sizes) {
            String sql = "INSERT INTO size (name, variant_id) VALUES (?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, size.getSize());
                ps.setLong(2, variant.getId());
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() != null) {
                size.setId(keyHolder.getKey().longValue());
            }

            String sql2 = "INSERT INTO product_size (product_id, size_id) VALUES (?, ?)";

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql2);
                ps.setLong(1, product.getId());
                ps.setLong(2, size.getId());
                return ps;
            });

        }
    }

    public void addColors(ProductDTO product, List<ColorDTO> colors, VariantDTO variant) {
        for (ColorDTO color : colors) {
            String sql = "INSERT INTO color (name, code, variant_id) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, color.getName());
                ps.setString(2, color.getCode());
                ps.setLong(3, variant.getId());
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() != null) {
                color.setId(keyHolder.getKey().longValue());
            }

            String sql2 = "INSERT INTO product_color (product_id, color_id) VALUES (?, ?)";

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql2);
                ps.setLong(1, product.getId());
                ps.setLong(2, color.getId());
                return ps;
            });

        }
    }

    public void addImages(ProductDTO product, List<String> images) {
        for (String image : images) {
            String sql = "INSERT INTO product_image (product_id, image) VALUES (?, ?)";

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setLong(1, product.getId());
                ps.setString(2, image);
                return ps;
            });

        }
    }

    public String handleFileUpload(MultipartFile file) {

        String fileName = file.getOriginalFilename();
        File uploadDir = new File(storageLocation);
        File destinationFile = new File(uploadDir, fileName);

        try {
//            file.transferTo(destinationFile.toPath());
            FileUtils.copyInputStreamToFile(file.getInputStream(), destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return imageHost + "/uploads/" + fileName;
    }

    public List<TransferBackDTO> getDataByCategory(String category, int offset, int limit) {

        String sql = "SELECT * FROM product WHERE category = ? LIMIT ? OFFSET ?";

        List<TransferBackDTO> backProducts = jdbcTemplate.query(sql, new Object[]{category, limit, offset}, transferRowMapper);

        for (TransferBackDTO backProduct : backProducts) {
            List<String> images = getImages(backProduct.getId());
            images.replaceAll(image -> imageHost + image);
            backProduct.setImages(images);
            backProduct.setColors(getColors(backProduct.getId()));
            backProduct.setSizes(getSizes(backProduct.getId()));
            backProduct.setVariants(getVariants(backProduct.getId()));
        }

        return backProducts;
    }

    public List<TransferBackDTO> getDataByKeyword(String keyword, int offset, int limit) {

        String sql = "SELECT * FROM product WHERE title LIKE ? LIMIT ? OFFSET ?";

        String keywordParam = "%" + keyword + "%";

        List<TransferBackDTO> backProducts = jdbcTemplate.query(sql, new Object[]{keywordParam, limit, offset}, transferRowMapper);

        for (TransferBackDTO backProduct : backProducts) {
            List<String> images = getImages(backProduct.getId());
            images.replaceAll(image -> imageHost + image);
            backProduct.setImages(images);
            backProduct.setColors(getColors(backProduct.getId()));
            backProduct.setSizes(getSizes(backProduct.getId()));
            backProduct.setVariants(getVariants(backProduct.getId()));
        }

        return backProducts;
    }

    public TransferBackDTO getDataById(Long id) {

        String sql = "SELECT * FROM product WHERE id = ?";

        TransferBackDTO backProduct = jdbcTemplate.queryForObject(sql, new Object[]{id}, transferRowMapper);

        Long productId = backProduct.getId();
        List<String> images = getImages(backProduct.getId());
        images.replaceAll(image -> imageHost + image);
        backProduct.setImages(images);
        backProduct.setColors(getColors(productId));
        backProduct.setSizes(getSizes(productId));
        backProduct.setVariants(getVariants(productId));

        return backProduct;
    }

    public List<TransferBackDTO> getDataByTitle(String title) {

//        log.info(title);
        String sql = "SELECT * FROM product WHERE title LIKE ?";

        String titleParam = "%" + title + "%";

        List<TransferBackDTO> backProducts = jdbcTemplate.query(sql, new Object[]{titleParam}, transferRowMapper);
//        for (TransferBackDTO backProduct : backProducts) {
//            log.info(backProduct.getTitle());
//            log.info(backProduct.getCategory());
//            log.info(backProduct.getDescription());
//        }
        return backProducts;
    }

    public List<TransferBackDTO> getDataAll(int offset, int limit) {

        String sql = "SELECT * FROM product LIMIT ? OFFSET ?";

        List<TransferBackDTO> backProducts = jdbcTemplate.query(sql, new Object[]{limit, offset}, transferRowMapper);

        for (TransferBackDTO backProduct : backProducts) {
            List<String> images = getImages(backProduct.getId());
            images.replaceAll(image -> imageHost + image);
            backProduct.setImages(images);
            backProduct.setColors(getColors(backProduct.getId()));
            backProduct.setSizes(getSizes(backProduct.getId()));
            backProduct.setVariants(getVariants(backProduct.getId()));
        }

        return backProducts;
    }

    private List<String> getImages(Long productId) {
        String sql = "SELECT image FROM product_image WHERE product_id = ?";
        return jdbcTemplate.query(sql, new Object[]{productId}, (rs, rowNum) -> rs.getString("image"));
    }

    private List<ColorDTO> getColors(Long productId) {
        String sql = "SELECT c.code, c.name FROM color c JOIN product_color pc ON c.id = pc.color_id WHERE pc.product_id = ?";
        return jdbcTemplate.query(sql, new Object[]{productId}, (rs, rowNum) -> {
            ColorDTO color = new ColorDTO(rs.getString("code"), rs.getString("name"));
            return color;
        });
    }

    private List<String> getSizes(Long productId) {
        String sql = "SELECT s.name FROM size s JOIN product_size ps ON s.id = ps.size_id WHERE ps.product_id = ?";
        return jdbcTemplate.query(sql, new Object[]{productId}, (rs, rowNum) -> rs.getString("name"));
    }

    private List<VariantDTO> getVariants(Long productId) {
        String sql = "SELECT c.code AS color_code, s.name AS size, v.stock FROM variant v JOIN color c ON v.id = c.variant_id JOIN size s ON v.id = s.variant_id WHERE v.product_id = ?";
        return jdbcTemplate.query(sql, new Object[]{productId}, (rs, rowNum) -> {
            VariantDTO variant = new VariantDTO(rs.getString("color_code"), rs.getString("size"), rs.getInt("stock"));
            return variant;
        });
    }

    public int countProductsByCategory(String category) {
        String sql = "SELECT COUNT(*) FROM product WHERE category = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{category}, Integer.class);
    }

    public int countProductsByKeyword(String keyword) {
        String sql = "SELECT COUNT(*) FROM product WHERE title LIKE ?";
        String keywordParam = "%" + keyword + "%";

        return jdbcTemplate.queryForObject(sql, new Object[]{keywordParam}, Integer.class);
    }

}









