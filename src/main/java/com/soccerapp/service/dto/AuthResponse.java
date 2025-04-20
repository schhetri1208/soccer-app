package com.soccerapp.service.dto;

public record AuthResponse(String token, String email, String fullName) {
}
