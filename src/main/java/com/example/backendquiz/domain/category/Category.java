package com.example.backendquiz.domain.category;

import com.example.backendquiz.common.BaseEntity;
import com.example.backendquiz.common.CategoryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private CategoryStatus name;

    private String description;

    private String iconUrl;

    private final boolean isActive = true;

    public Category(CategoryStatus name, String description, String iconUrl) {
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
    }
}
