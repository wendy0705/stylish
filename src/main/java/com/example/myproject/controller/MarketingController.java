package com.example.myproject.controller;

import com.example.myproject.dto.CampaignDTO;
import com.example.myproject.repository.CampaignRepository;
import com.example.myproject.repository.ProductRepository;
import com.example.myproject.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/1.0/marketing")
public class MarketingController {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CampaignRepository campaignRepository;
    @Autowired
    CampaignService campaignService;

    @PostMapping("/campaigns")
    public ResponseEntity<String> createCampaign(@RequestParam("product_id") Long productId,
                                                 @RequestParam("picture") MultipartFile picture,
                                                 @RequestParam("story") String story) {

        String picturePath = productRepository.handleFileUpload(picture);

        CampaignDTO campaign = new CampaignDTO();
        campaign.setProductId(productId);
        campaign.setPicture(picturePath);
        campaign.setStory(story);

        campaignService.saveCampaign(campaign);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/campaigns")
    public ResponseEntity<Map<String, Object>> getAllCampaigns() {
        Map<String, Object> response = new HashMap<>();
        List<CampaignDTO> campaigns = campaignService.getAllCampaigns();
        response.put("data", campaigns);
        return ResponseEntity.ok(response);
    }

}

