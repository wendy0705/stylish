package com.example.myproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ColorDTO {
    @JsonIgnore
    private Long id;
    private String name;
    public String code;

    public ColorDTO() {
    }

    public ColorDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
