package com.soccerapp.service.dto;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public record GameResponse(Long groupId, String date, String time, String location, String createdBy) {
}
