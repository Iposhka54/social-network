package com.iposhka.socialnetworkapi.service;

import com.iposhka.socialnetworkapi.dto.request.UserRequestDto;
import com.iposhka.socialnetworkapi.dto.response.UserResponseDto;
import com.iposhka.socialnetworkapi.exception.DatabaseException;
import com.iposhka.socialnetworkapi.exception.UserAlreadyExistsException;
import com.iposhka.socialnetworkapi.mapper.UserMapper;
import com.iposhka.socialnetworkapi.model.User;
import com.iposhka.socialnetworkapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto create(UserRequestDto userRequestDto) {
        userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        User user = userMapper.toEntity(userRequestDto);

        try {
            userRepository.save(user);

            log.info("User with username {} saved to the database", user.getUsername());

            return userMapper.toDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User with that username already exists");
        } catch (Exception e) {
            throw new DatabaseException("Any problems with database");
        }
    }
}