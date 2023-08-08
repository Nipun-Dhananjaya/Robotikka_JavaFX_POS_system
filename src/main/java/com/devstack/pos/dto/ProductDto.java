package com.devstack.pos.dto;

public class ProductDto {
    private int code;

    public ProductDto(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public ProductDto() {
    }

    private String description;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
