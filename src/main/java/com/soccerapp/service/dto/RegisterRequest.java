package com.soccerapp.service.dto;

public record RegisterRequest(String firstName, String lastName, String email, String password, String location) {
}
