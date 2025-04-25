package com.soccerapp.service;

import com.soccerapp.model.*;
import com.soccerapp.repository.GameParticipantRepository;
import com.soccerapp.repository.GameRepository;
import com.soccerapp.repository.GroupRepository;
import com.soccerapp.repository.UserRepository;
import com.soccerapp.service.dto.AssignTeamRequest;
import com.soccerapp.service.dto.CreateGameRequest;
import com.soccerapp.service.dto.GameResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GameService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameParticipantRepository gameParticipantRepository;

    @Autowired
    private NotificationService notificationService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

    public GameService(GroupRepository groupRepository, UserRepository userRepository, GameRepository gameRepository, GameParticipantRepository gameParticipantRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.gameParticipantRepository = gameParticipantRepository;
    }

    public GameResponse scheduleGame(CreateGameRequest request, String creatorEmail) {
        User creator = userRepository.findByEmail(creatorEmail);
        Group group = groupRepository.findById(request.groupId()).orElseThrow();

        Game game = new Game();
        game.setGroup(group);
        game.setCreatedBy(creator);
        game.setGameDate(LocalDate.parse((request.date())));
        game.setGameTime(LocalTime.parse(request.time(),formatter));
        game.setLocation(request.location());
        game.setFieldLat(request.lat());
        game.setFieldLng(request.lng());

        gameRepository.save(game);

        notificationService.sendNotification(request.groupId(),"New game scheduled for " + game.getGameDate() + " at " + game.getLocation());

        return new GameResponse(game.getId(), game.getGameDate().toString(),game.getGameTime().format(formatter), game.getLocation(), creator.getFirstName());
    }

    public List<GameResponse> getGameForGroup(Long groupId) {
        return gameRepository.findByGroupId(groupId).stream()
                .map(g -> new GameResponse(g.getId(), g.getGameDate().toString() , g.getGameTime().format(formatter) , g.getLocation() , g.getCreatedBy().getFirstName()))
                .toList();
    }

    public void assignTeam(Long gameId, AssignTeamRequest request) {
        GameParticipantId id = new GameParticipantId(gameId, request.userId());

        GameParticipant participant = gameParticipantRepository.findById(id).orElseThrow(() -> new RuntimeException("User has not RSVPed for this game."));
        participant.setTeam(request.team().toUpperCase());
        gameParticipantRepository.save(participant);
    }
}
