package com.iposhka.socialnetworkapi.controller;

import com.iposhka.socialnetworkapi.dto.request.UserRequestDto;
import com.iposhka.socialnetworkapi.dto.response.UserResponseDto;
import com.iposhka.socialnetworkapi.service.AuthService;
import com.iposhka.socialnetworkapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/sign-in")
    public ResponseEntity<UserResponseDto> signIn(@RequestBody @Valid UserRequestDto userDto,
                                                  HttpServletRequest request,
                                                  @CurrentSecurityContext SecurityContext context) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));

        context.setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .username(userDto.getUsername())
                .build();

        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody @Valid UserRequestDto userDto, HttpServletRequest request) {
        UserResponseDto userResponseDto = authService.create(userDto);

        var authenticationToken = new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return ResponseEntity.created(URI.create("api/auth/me")).body(userResponseDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<UserResponseDto> maybeUser = userService.findByUsername(userDetails.getUsername());

        return maybeUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}