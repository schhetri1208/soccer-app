package com.soccerapp.service;

import com.soccerapp.model.*;
import com.soccerapp.repository.*;
import com.soccerapp.service.dto.AssignTeamRequest;
import com.soccerapp.service.dto.CreateGameRequest;
import com.soccerapp.service.dto.GameResponse;
import com.soccerapp.service.dto.GameUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
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
    private final GroupMemberRepository groupMemberRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SendGridService sendGridService;

    private static final DateTimeFormatter TIME_FORMATTER  = DateTimeFormatter.ofPattern("h:mm a");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    public GameService(GroupRepository groupRepository, UserRepository userRepository, GameRepository gameRepository, GameParticipantRepository gameParticipantRepository, GroupMemberRepository groupMemberRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.gameParticipantRepository = gameParticipantRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    public GameResponse scheduleGame(CreateGameRequest request, String creatorEmail) {
        User creator = userRepository.findByEmail(creatorEmail);
        Group group = groupRepository.findById(request.groupId()).orElseThrow();

        Game game = new Game();
        game.setGroup(group);
        game.setCreatedBy(creator);
        game.setGameDate(LocalDate.parse((request.date())));
        game.setGameTime(LocalTime.parse(request.time(),TIME_FORMATTER));
        game.setLocation(request.location());
        game.setFieldLat(request.lat());
        game.setFieldLng(request.lng());

        gameRepository.save(game);

        List<GroupMember> members = groupMemberRepository.findByGroupId(request.groupId());

        for (GroupMember groupMember : members) {
            String email = groupMember.getUser().getEmail();
            try {
                String htmlBody = sendGridService.buildGameEmail(game, groupMember.getUser().getFirstName());
                sendGridService.sendEmail(email, "📢 New Game Scheduled!", htmlBody);
            } catch (IOException e) {
                System.out.println("Failed to send email to " + email + ": " + e.getMessage());
            }
        }

        notificationService.sendNotification(request.groupId(),"New game scheduled for " + game.getGameDate() + " at " + game.getLocation());

        return new GameResponse(game.getId(), game.getGameDate().format(DATE_FORMATTER),game.getGameTime().format(TIME_FORMATTER), game.getLocation(), creator.getFirstName());
    }

    public List<GameResponse> getGameForGroup(Long groupId) {
        return gameRepository.findByGroupId(groupId).stream()
                .map(g -> new GameResponse(g.getId(), g.getGameDate().format(DATE_FORMATTER) , g.getGameTime().format(TIME_FORMATTER) , g.getLocation() , g.getCreatedBy().getFirstName()))
                .toList();
    }

    public void assignTeam(Long gameId, AssignTeamRequest request) {
        GameParticipantId id = new GameParticipantId(gameId, request.userId());

        GameParticipant participant = gameParticipantRepository.findById(id).orElseThrow(() -> new RuntimeException("User has not RSVPed for this game."));
        participant.setTeam(request.team().toUpperCase());
        gameParticipantRepository.save(participant);
    }

    public void updateGame(Long gameId, GameUpdateRequest request, String email) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        if (!game.getCreatedBy().getEmail().equals(email)) {
            throw new AccessDeniedException("Only the creator can update the game.");
        }
        game.setGameDate(request.gameDate());
        game.setGameTime(request.gameTime());
        game.setLocation(request.location());

        gameRepository.save(game);

        notificationService.sendNotification(game.getGroup().getId(), "Game updated: " + game.getLocation() + " on " + game.getGameDate().format(DATE_FORMATTER));

        List<GroupMember> members = groupMemberRepository.findByGroupId(game.getGroup().getId());

        for (GroupMember groupMember : members) {
            User user = groupMember.getUser();
            String userEmail = user.getEmail();

            try {
                String htmlBody = sendGridService.buildGameEmail(game, user.getFirstName());
                sendGridService.sendEmail(email, "Game Updated!", htmlBody);
            } catch (IOException e) {
                System.out.println("Failed to send email to " + email + ": " + e.getMessage());
            }
        }

    }

    public void cancelGame(Long gameId, String userEmail) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        if (!game.getCreatedBy().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("Only the creator can cancel the game.");
        }
        gameRepository.delete(game);

        notificationService.sendNotification(game.getGroup().getId(), "Game canceled: " + game.getLocation() + " on " + game.getGameDate().format(DATE_FORMATTER));
    }
}
