package com.iposhka.socialnetworkapi.dto.response;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ErrorResponseDto {
    private String message;
    Map<String, String> errors = new HashMap<>();

    public ErrorResponseDto(String message) {
        this.message = message;
    }

    public void putError(String key, String error){
        errors.put(key, error);
    }
}