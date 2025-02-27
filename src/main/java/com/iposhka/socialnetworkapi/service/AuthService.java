package com.iposhka.socialnetworkapi.service;

import com.iposhka.socialnetworkapi.dto.request.UserRequestDto;
import com.iposhka.socialnetworkapi.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;

    public UserResponseDto create(UserRequestDto userRequestDto) {
        return userService.create(userRequestDto);
    }
}