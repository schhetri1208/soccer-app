package com.soccerapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@Table(name ="games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User createdBy;

    private LocalDate gameDate;
    private LocalTime gameTime;
    private String location;
    private double fieldLat;
    private double fieldLng;
    private LocalDateTime createdAt = LocalDateTime.now();

}
