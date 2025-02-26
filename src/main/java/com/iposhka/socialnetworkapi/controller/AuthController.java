package com.iposhka.socialnetworkapi.controller;

import com.iposhka.socialnetworkapi.dto.request.UserRequestDto;
import com.iposhka.socialnetworkapi.dto.response.UserResponseDto;
import com.iposhka.socialnetworkapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("sign-in")
    public ResponseEntity<UserResponseDto> signIn(@RequestBody UserRequestDto userDto) {
        return ResponseEntity.ok(null);
    }

    @PostMapping("sign-up")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody UserRequestDto userDto) {
        UserResponseDto userResponseDto = authService.create(userDto);

        return ResponseEntity.status(201).body(userResponseDto);
    }
}