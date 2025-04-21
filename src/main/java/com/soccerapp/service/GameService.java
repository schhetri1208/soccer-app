package com.soccerapp.service;

import com.soccerapp.model.Game;
import com.soccerapp.model.Group;
import com.soccerapp.model.User;
import com.soccerapp.repository.GameRepository;
import com.soccerapp.repository.GroupRepository;
import com.soccerapp.repository.UserRepository;
import com.soccerapp.service.dto.CreateGameRequest;
import com.soccerapp.service.dto.GameResponse;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class GameService {

    private GroupRepository groupRepository;
    private UserRepository userRepository;
    private GameRepository gameRepository;

    public GameService(GroupRepository groupRepository, UserRepository userRepository, GameRepository gameRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public GameResponse scheduleGame(CreateGameRequest request, String creatorEmail) {
        User creator = userRepository.findByEmail(creatorEmail);
        Group group = groupRepository.findById(request.groupId()).orElseThrow();

        Game game = new Game();
        game.setGroup(group);
        game.setCreatedBy(creator);
        game.setGameDate(LocalDate.now().with(DayOfWeek.valueOf(request.day().toUpperCase())));
        game.setGameTime(LocalTime.parse(request.time()));
        game.setLocation(request.location());
        game.setFieldLat(request.lat());
        game.setFieldLng(request.lng());

        gameRepository.save(game);

        return new GameResponse(game.getId(), game.getGameDate().toString(),game.getGameTime().toString(), game.getLocation(), creator.getFirstName());
    }

    public List<GameResponse> getGameForGroup(Long groupId) {
        return gameRepository.getGameByGroup(groupId).stream()
                .map(g -> new GameResponse(g.getId(), g.getGameDate().toString() , g.getGameDate().toString() , g.getLocation() , g.getCreatedBy().getFirstName()))
                .toList();
    }
}
