package com.soccerapp.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record GameUpdateRequest(LocalDate gameDate, LocalTime gameTime, String location) {
}
