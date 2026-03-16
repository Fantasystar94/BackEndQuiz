package com.example.backendquiz.domain.user;

import com.example.backendquiz.common.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(String email, String nickname, String provider, String providerId, UserRole userRole) {
        log.info("==userService==");
        User user = new User(email, nickname, provider, providerId, userRole);
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User getUser(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found"));
    }
}
