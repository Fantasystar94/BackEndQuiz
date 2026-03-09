package com.example.backendquiz.domain.user;

import com.example.backendquiz.common.BaseEntity;
import com.example.backendquiz.common.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    private UserRole role;

    public User(String email, String nickname, String provider, String providerId, UserRole role) {
        this.email = email;
        this.nickname = nickname;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
    }
}
