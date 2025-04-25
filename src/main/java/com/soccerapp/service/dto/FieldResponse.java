package com.soccerapp.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public record FieldResponse(
    Long id,
    String name,
     double latitude,
     double longitude
) {}
