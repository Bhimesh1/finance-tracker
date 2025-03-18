package com.personalfinance.finance_tracker.dto.request;

import com.personalfinance.finance_tracker.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoryRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Category.CategoryType type;

    private String color;

    private String icon;

    // Default constructor
    public CategoryRequest() {
    }

    // All-args constructor
    public CategoryRequest(String name, String description, Category.CategoryType type,
                           String color, String icon) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.color = color;
        this.icon = icon;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category.CategoryType getType() {
        return type;
    }

    public void setType(Category.CategoryType type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}