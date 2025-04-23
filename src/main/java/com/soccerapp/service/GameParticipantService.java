package com.soccerapp.service;

import com.soccerapp.model.Game;
import com.soccerapp.model.GameParticipant;
import com.soccerapp.model.GameParticipantId;
import com.soccerapp.model.User;
import com.soccerapp.repository.GameParticipantRepository;
import com.soccerapp.repository.GameRepository;
import com.soccerapp.repository.UserRepository;
import com.soccerapp.service.dto.RsvpRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GameParticipantService {

    private final GameParticipantRepository gameParticipantRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public GameParticipantService(GameParticipantRepository gameParticipantRepository, UserRepository userRepository, GameRepository gameRepository) {
        this.gameParticipantRepository = gameParticipantRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public void rsvpToGame(String email, Long gameId, RsvpRequest request) {
        User user = userRepository.findByEmail(email);
        Game game = gameRepository.findById(gameId).orElseThrow();

        GameParticipant participant = new GameParticipant();
        participant.setId(new GameParticipantId(gameId, user.getId()));
        participant.setGame(game);
        participant.setUser(user);
        participant.setTeam(null);
        participant.setStatus(request.status());
        System.out.println("Status: " + request.status());
        participant.setRespondedAt(LocalDateTime.now());

        gameParticipantRepository.save(participant);
    }
}
