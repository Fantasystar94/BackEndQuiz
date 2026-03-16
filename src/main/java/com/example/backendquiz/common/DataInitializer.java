package com.example.backendquiz.common;

import com.example.backendquiz.domain.category.Category;
import com.example.backendquiz.domain.category.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        for (CategoryStatus categoryStatus : CategoryStatus.values()) {
            if (!categoryRepository.existsByName(categoryStatus)) {
                categoryRepository.save(new Category(categoryStatus, getDescription(categoryStatus),String.valueOf(categoryStatus)));
            }
        }
    }
    private String getDescription(CategoryStatus status) {
        return switch (status) {
            case SPRING -> "스프링 프레임워크 관련 문제";
            case JAVA -> "자바 언어 관련 문제";
            case DATABASE -> "데이터베이스 관련 문제";
            case ALGORITHM -> "알고리즘 관련 문제";
        };
    }
}
