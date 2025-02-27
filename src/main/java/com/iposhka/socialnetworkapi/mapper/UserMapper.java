package com.iposhka.socialnetworkapi.mapper;

import com.iposhka.socialnetworkapi.dto.request.UserRequestDto;
import com.iposhka.socialnetworkapi.dto.response.UserResponseDto;
import com.iposhka.socialnetworkapi.model.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    AppUser toEntity(UserRequestDto dto);

    UserResponseDto toDto(AppUser appUser);
}