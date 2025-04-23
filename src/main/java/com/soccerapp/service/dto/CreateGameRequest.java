package com.soccerapp.service.dto;

import org.springframework.stereotype.Service;

public record CreateGameRequest(Long groupId, String date, String time, String location, double lat, double lng) {
}
