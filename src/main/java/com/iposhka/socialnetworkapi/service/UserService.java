package com.iposhka.socialnetworkapi.service;

import com.iposhka.socialnetworkapi.dto.request.UserRequestDto;
import com.iposhka.socialnetworkapi.dto.response.UserResponseDto;
import com.iposhka.socialnetworkapi.exception.DatabaseException;
import com.iposhka.socialnetworkapi.exception.UserAlreadyExistsException;
import com.iposhka.socialnetworkapi.mapper.UserMapper;
import com.iposhka.socialnetworkapi.model.AppUser;
import com.iposhka.socialnetworkapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto create(UserRequestDto userRequestDto) {
        userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        AppUser user = userMapper.toEntity(userRequestDto);

        try {
            userRepository.save(user);

            log.info("AppUser with username {} saved to the database", user.getUsername());

            return userMapper.toDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User with that username already exists");
        } catch (Exception e) {
            throw new DatabaseException("Any problems with database");
        }
    }

    public Optional<UserResponseDto> findByUsername(String username){
        return userRepository.findByUsername(username)
                .map(userMapper::toDto);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> User.withUsername(username)
                        .password(user.getPassword())
                        .authorities(Collections.emptyList())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }
}
