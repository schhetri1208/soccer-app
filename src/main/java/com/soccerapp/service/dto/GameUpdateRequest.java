package com.soccerapp.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public record GameUpdateRequest(LocalDate gameDate, @JsonFormat(pattern = "hh:mm a") LocalTime gameTime, String location) {
}
