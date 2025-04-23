package com.soccerapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="game_participant")
public class GameParticipant {
    @EmbeddedId
    private GameParticipantId id;

    @ManyToOne
    @MapsId("gameId")
    private Game game;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @Column(name = "status")
    private String status;
    private String team;
    private LocalDateTime respondedAt = LocalDateTime.now();

}