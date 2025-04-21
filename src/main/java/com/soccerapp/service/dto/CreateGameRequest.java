package com.soccerapp.service.dto;

import org.springframework.stereotype.Service;

@Service
public record CreateGameRequest(Long groupId, String day, String time, String location, double lat, double lng) {
}
