package com.example.backendquiz.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/login/success")
    public ResponseEntity<String> loginSuccess(@RequestParam String token) {
        return ResponseEntity.ok(token);
    }
}
