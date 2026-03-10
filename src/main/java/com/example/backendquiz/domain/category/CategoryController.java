package com.example.backendquiz.domain.category;

import com.example.backendquiz.common.CategoryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public Category createCategory() {

        return categoryService.createCategory(
                CategoryStatus.SPRING,
                "Spring Framework Interview",
                "spring-icon"
        );
    }
}
