package com.example.backendquiz.domain.wrongnote;

import com.example.backendquiz.auth.AuthUser;
import com.example.backendquiz.domain.user.User;
import com.example.backendquiz.domain.wrongnote.dto.WrongNoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wrong-notes")
@RequiredArgsConstructor
public class WrongNoteController {

    private final WrongNoteService wrongNoteService;

    // 내 오답노트 목록
    @GetMapping
    public ResponseEntity<List<WrongNoteResponse>> getMyWrongNotes(
            @AuthenticationPrincipal AuthUser authUser) {

        return ResponseEntity.ok(wrongNoteService.getMyWrongNotes(authUser));
    }

    // 오답노트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser authUser) {
        wrongNoteService.delete(id, authUser);
        return ResponseEntity.noContent().build();
    }
}
