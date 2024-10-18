package com.example.myproject.controller;

import com.example.myproject.dto.TransferBackDTO;
import com.example.myproject.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/1.0/products")
public class PageController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{category}")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(value = "paging", required = false, defaultValue = "0") int paging) {
        log.info("getProductsByCategory");
        int pageSize = 6;
        Map<String, Object> response = productService.getProductsByCategory(category, paging, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> getProductsByKeyword(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) String title,
            @RequestParam(value = "paging", required = false, defaultValue = "0") int paging) {

        Map<String, Object> response;

        if (keyword == null || keyword.isEmpty()) { //keyword doesn't exist
            response = productService.getProductsByTitle(title); //use for campaign
        } else {
            int pageSize = 6;
            response = productService.getProductsByKeyword(keyword, paging, pageSize);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/details")
    public ResponseEntity<Map<String, TransferBackDTO>> getProductsById(
            @RequestParam(required = false, defaultValue = "") Long id) {
        Map<String, TransferBackDTO> response = productService.getProductsById(id);
        return ResponseEntity.ok(response);
    }
}