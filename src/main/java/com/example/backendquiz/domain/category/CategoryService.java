package com.example.backendquiz.domain.category;

import com.example.backendquiz.common.CategoryStatus;
import com.example.backendquiz.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CommonResponse<Void> createCategory(CategoryStatus name, String description, String icon) {

        if (categoryRepository.existsByName(name)) {
            throw new RuntimeException("이미 존재하는 카테고리입니다.");
        }

        Category category = new Category(name, description, icon);

        categoryRepository.save(category);

        return CommonResponse.successWithData("success Create Category",null);
    }
}
