package com.example.backendquiz.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    //String email, String nickname, String provider, String providerId, UserRole userRole
    private String email;
    private String nickname;
    private String provider;
    private String providerId;
    private String userRole;


}
