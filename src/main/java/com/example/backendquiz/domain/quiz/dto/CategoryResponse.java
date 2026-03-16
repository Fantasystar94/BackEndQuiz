package com.example.backendquiz.domain.quiz.dto;

import com.example.backendquiz.common.CategoryStatus;
import com.example.backendquiz.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private CategoryStatus name;
    private String description;

    public CategoryResponse(Category category) {
        this.name = category.getName();
        this.description = category.getDescription();
    }
}
