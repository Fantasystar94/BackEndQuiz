package com.example.backendquiz.config.oauth;

import com.example.backendquiz.common.UserRole;
import com.example.backendquiz.domain.user.User;
import com.example.backendquiz.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuthUserInfo userInfo = new OAuthUserInfo(oAuth2User.getAttributes());

        userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> userRepository.save(new User(
                        userInfo.getEmail(),
                        userInfo.getNickname(),
                        provider,
                        userInfo.getProviderId(),
                        UserRole.USER
                )));

        return new DefaultOAuth2User(
                Collections.singleton(() -> "ROLE_USER"),
                oAuth2User.getAttributes(),
                "email"
        );
    }
}
