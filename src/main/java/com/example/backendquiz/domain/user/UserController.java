package com.example.backendquiz.domain.user;

import com.example.backendquiz.common.CommonResponse;
import com.example.backendquiz.common.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public CommonResponse<User> createUser() {
        User user = userService.createUser(
                "test@test.com",
                "testUser",
                "provider",
                "providerId",
                UserRole.USER
        );
        return CommonResponse.successWithData("createUserSuccess",user);
    }
}
