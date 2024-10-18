package com.example.myproject.service;

import com.example.myproject.dto.CampaignDTO;
import com.example.myproject.repository.CampaignRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CampaignService {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CampaignRepository campaignRepository;

    private static final String CACHE_KEY = "campaigns";

    public List<CampaignDTO> getAllCampaigns() {
        // 1. ask cache
        // - none: go to database, and save it to cache
        // - exists: hit, get from cache
        String cachedCampaigns = cacheService.getCampaignDataFromCache(CACHE_KEY); //get cache data, Json
        if (cachedCampaigns != null) { //data null or redis disconnect
            log.info("Data retrieved from Redis cache.");
            return convertJsonToCampaignList(cachedCampaigns); //return cache data
        }

        List<CampaignDTO> campaigns = campaignRepository.getAllCampaigns(); //get sql data
        log.info("Data retrieved from the database.");

        String campaignsJson = convertCampaignListToJson(campaigns);
        cacheService.setCampaignDataToCache(CACHE_KEY, campaignsJson); //set sql data to cache

        return campaigns; //return sql data
    }

    public void saveCampaign(CampaignDTO campaignDTO) {
        campaignRepository.save(campaignDTO);
        log.info("Data saved to the database. Cache will be cleared.");

        cacheService.clearCache(CACHE_KEY); //update campaign data in database, so clean origin data in cache
    }

    private List<CampaignDTO> convertJsonToCampaignList(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<CampaignDTO>>() {
            });
        } catch (Exception e) {
            log.error("Error converting JSON to CampaignDTO list", e);
            return Collections.emptyList();
        }
    }

    private String convertCampaignListToJson(List<CampaignDTO> campaigns) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(campaigns);
        } catch (Exception e) {
            log.error("Error converting CampaignDTO list to JSON", e);
            return "";
        }
    }
}
