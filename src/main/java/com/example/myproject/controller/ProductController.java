package com.example.myproject.controller;


import com.example.myproject.dto.TransferDTO;
import com.example.myproject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/adminproduct.html")
    public String product() {
        return "adminproduct";
    }

    @GetMapping("/campaign.html")
    public String campaign() {
        return "campaign";
    }

    @GetMapping("/checkout.html")
    public String checkout() {
        return "checkout";
    }


    @PostMapping("/create")
    public ResponseEntity<String> create(@ModelAttribute TransferDTO transferDTO,
                                         @RequestParam("mainImage") MultipartFile mainImage,
                                         @RequestParam("images") List<MultipartFile> images) {

        boolean success = productService.createProduct(transferDTO, mainImage, images);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}


