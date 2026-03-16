package com.example.backendquiz.domain.category;


import com.example.backendquiz.common.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(CategoryStatus name);
}
