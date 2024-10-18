package com.example.myproject.dto;

public class SizeDTO {
    private Long id;

    private String size;

    public SizeDTO() {
    }

    public SizeDTO(String size) {
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


}
