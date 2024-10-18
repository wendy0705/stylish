package com.example.myproject.service;

import com.example.myproject.dto.*;
import com.example.myproject.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    int offset;

    public boolean createProduct(TransferDTO transferDTO, MultipartFile mainImage, List<MultipartFile> images) {
        try {
            String mainImageUrl = productRepository.handleFileUpload(mainImage);

            List<String> imagesUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                String imageUrl = productRepository.handleFileUpload(image);
                imagesUrls.add(imageUrl);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<ColorDTO> colors = objectMapper.readValue(transferDTO.getColors(), new TypeReference<List<ColorDTO>>() {
            });
            List<SizeDTO> sizes = objectMapper.readValue(transferDTO.getSizes(), new TypeReference<List<SizeDTO>>() {
            });

            ProductDTO product = new ProductDTO(transferDTO.getCategory(), transferDTO.getTitle(), transferDTO.getDescription(), transferDTO.getPrice(), transferDTO.getTexture(), transferDTO.getWash(), transferDTO.getPlace(), transferDTO.getNote(), transferDTO.getStory(), mainImageUrl);
            VariantDTO variant = new VariantDTO(transferDTO.getStocks());

            productRepository.addProduct(product);
            productRepository.addVariant(product, variant);
            productRepository.addSizes(product, sizes, variant);
            productRepository.addColors(product, colors, variant);
            productRepository.addImages(product, imagesUrls);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> getProductsByCategory(String category, int page, int pageSize) {
        if (page == 0) {
            offset = 0;
        } else {
            offset = page * pageSize;
        }

        Map<String, Object> response = new HashMap<>();

        if (category.equals("all")) {
            List<TransferBackDTO> backDataAll = productRepository.getDataAll(offset, pageSize);

            response.put("data", backDataAll);
        } else {
            List<TransferBackDTO> backData = productRepository.getDataByCategory(category, offset, pageSize);
            response.put("data", backData);
            int totalProducts = productRepository.countProductsByCategory(category);

            response.put("data", backData);
            if (offset + pageSize < totalProducts) {
                response.put("next_paging", page + 1);
            }
        }

        return response;
    }

    public Map<String, Object> getProductsByKeyword(String keyword, int page, int pageSize) {
        if (page == 0) {
            offset = 0;
        } else {
            offset = page * pageSize;
        }
        Map<String, Object> response = new HashMap<>();

        List<TransferBackDTO> backData = productRepository.getDataByKeyword(keyword, offset, pageSize);
        int totalProducts = productRepository.countProductsByKeyword(keyword);

        response.put("data", backData);
        if (offset + pageSize < totalProducts) {
            response.put("next_paging", page + 1);
        }

        return response;
    }

    public Map<String, Object> getProductsByTitle(String title) {

        Map<String, Object> response = new HashMap<>();
        List<TransferBackDTO> backData = productRepository.getDataByTitle(title);
        response.put("data", backData);
        return response;

    }

    public Map<String, TransferBackDTO> getProductsById(Long id) {

        TransferBackDTO backData = productRepository.getDataById(id);
        Map<String, TransferBackDTO> backProductMap = new HashMap<>();
        backProductMap.put("data", backData);

        return backProductMap;
    }

}
