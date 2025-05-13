package com.soccerapp.service.dto;

public record UserProfileRequest(String firstName, String lastName, String location, String currentPassword, String newPassword) {}

