package com.example.myproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VariantDTO {
    @JsonIgnore
    private Long id;
    private String color_code;
    private Integer stock;
    private String size;

    public VariantDTO(Integer stock) {
        this.stock = stock;
    }

    public VariantDTO(String colorCode, String size, Integer stock) {
        this.color_code = colorCode;
        this.size = size;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stocks) {
        this.stock = stocks;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}