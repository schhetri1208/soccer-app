package com.soccerapp.controller;

import com.soccerapp.model.Game;
import com.soccerapp.model.GameComment;
import com.soccerapp.model.GameParticipant;
import com.soccerapp.repository.GameParticipantRepository;
import com.soccerapp.security.JwtUtil;
import com.soccerapp.service.GameCommentService;
import com.soccerapp.service.GameParticipantService;
import com.soccerapp.service.GameService;
import com.soccerapp.service.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/games")
public class GameController {

    private final GameService gameService;
    private final JwtUtil jwtUtil;
    private final GameParticipantService gameParticipantService;
    private final GameCommentService gameCommentService;

    public GameController(GameService gameService, JwtUtil jwtUtil, GameParticipantService gameParticipantService, GameCommentService gameCommentService) {
        this.gameService = gameService;
        this.jwtUtil = jwtUtil;
        this.gameParticipantService = gameParticipantService;
        this.gameCommentService = gameCommentService;
    }

    @PostMapping
    public ResponseEntity<GameResponse> scheduleGame (@RequestHeader ("Authorization") String token,
                                                      @RequestBody CreateGameRequest request) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));

        return ResponseEntity.ok(gameService.scheduleGame(request, email));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<GameResponse>> getGameForGroup(@RequestHeader ("Authorization") String token,
                                                              @PathVariable Long groupId) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        return ResponseEntity.ok(gameService.getGameForGroup(groupId));
    }

    @PostMapping("/{gameId}/rsvp")
    public ResponseEntity<String> rsvpToGame(@PathVariable Long gameId,
                                             @RequestHeader("Authorization") String token,
                                             @RequestBody RsvpRequest request) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        gameParticipantService.rsvpToGame(email,gameId, request);
        System.out.println("Received status: " + request.status());
        return ResponseEntity.ok("RSVP recorded.");
    }

    @PostMapping("/{gameId}/comments")
    public ResponseEntity<String> addComment(@RequestHeader("Authorization") String token,
                                                  @PathVariable Long gameId,
                                                  @RequestBody GameCommentRequest request) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        gameCommentService.addComment(gameId, request.content(), null,email);
        return ResponseEntity.ok("Comment added");
    }

    //Reply a comment
    @PostMapping("/{gameId}/comments/{parentId}")
    public ResponseEntity<String> replyComment(@RequestHeader("Authorization") String token,
                                             @PathVariable Long gameId,
                                               @PathVariable Long parentId,
                                             @RequestBody GameCommentRequest request) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        gameCommentService.addComment(gameId, request.content(), parentId,email);
        return ResponseEntity.ok("Reply added ");
    }

    //Get all comments
    @GetMapping("/{gameId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@RequestHeader ("Authorization") String token,
                                                                 @PathVariable Long gameId) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        return ResponseEntity.ok(gameCommentService.getThreadedComments(gameId));
    }

    //Like comment
    @PutMapping("/comments/{commentId}/like")
    public ResponseEntity<?> likeComment (@RequestHeader ("Authorization") String token,
                                                        @PathVariable Long commentId) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        gameCommentService.likeComment(commentId);
        return ResponseEntity.ok("Liked");
    }

    @PostMapping("/{gameId}/assign-team")
    public ResponseEntity<String> assignTeam(@RequestHeader("Authorization") String token,
                                                      @PathVariable Long gameId,
                                                      @RequestBody AssignTeamRequest request) {

        String email = jwtUtil.getEmailFromToken(token.substring(7));
        gameService.assignTeam(gameId, request);
        return ResponseEntity.ok("Team assigned successfully");
    }
}
