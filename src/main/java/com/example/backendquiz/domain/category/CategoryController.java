package com.example.backendquiz.domain.category;

import com.example.backendquiz.common.CategoryStatus;
import com.example.backendquiz.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CommonResponse<Void> createCategory() {

        return categoryService.createCategory(
                CategoryStatus.SPRING,
                "Spring Framework Interview",
                "spring-icon"
        );
    }
}
