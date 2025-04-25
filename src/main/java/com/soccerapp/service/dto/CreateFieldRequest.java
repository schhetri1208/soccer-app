package com.soccerapp.service.dto;

public record CreateFieldRequest(String name, double latitude, double longitude, String city, String state, String zip) {
}
