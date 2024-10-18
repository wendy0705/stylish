package com.example.myproject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CampaignDTO {
    private Long id;
    @JsonProperty("product_id")
    private Long productId;
    private String picture;
    private String story;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}
