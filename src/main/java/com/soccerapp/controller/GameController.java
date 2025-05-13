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
import com.soccerapp.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<GameResponse> scheduleGame (HttpServletRequest httpServletRequest,
                                                      @RequestBody CreateGameRequest request) {
        String email = RequestUtil.getEmail(httpServletRequest);

        return ResponseEntity.ok(gameService.scheduleGame(request, email));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<GameResponse>> getGameForGroup(HttpServletRequest httpServletRequest,
                                                              @PathVariable Long groupId) {
        String email = RequestUtil.getEmail(httpServletRequest);
        return ResponseEntity.ok(gameService.getGameForGroup(groupId));
    }

    @PostMapping("/{gameId}/rsvp")
    public ResponseEntity<String> rsvpToGame(HttpServletRequest httpServletRequest,
                                             @PathVariable Long gameId,
                                             @RequestBody RsvpRequest request) {
        String email = RequestUtil.getEmail(httpServletRequest);
        gameParticipantService.rsvpToGame(email,gameId, request);
        System.out.println("Received status: " + request.status());
        return ResponseEntity.ok("RSVP recorded.");
    }

    @PostMapping("/{gameId}/comments")
    public ResponseEntity<String> addComment(HttpServletRequest httpServletRequest,
                                                  @PathVariable Long gameId,
                                                  @RequestBody GameCommentRequest request) {
        String email = RequestUtil.getEmail(httpServletRequest);
        gameCommentService.addComment(gameId, request.content(), null,email);
        return ResponseEntity.ok("Comment added");
    }

    //Reply a comment
    @PostMapping("/{gameId}/comments/{parentId}")
    public ResponseEntity<String> replyComment(HttpServletRequest httpServletRequest,
                                             @PathVariable Long gameId,
                                               @PathVariable Long parentId,
                                             @RequestBody GameCommentRequest request) {
        String email = RequestUtil.getEmail(httpServletRequest);
        gameCommentService.addComment(gameId, request.content(), parentId,email);
        return ResponseEntity.ok("Reply added ");
    }

    //Get all comments
    @GetMapping("/{gameId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(HttpServletRequest httpServletRequest,
                                                                 @PathVariable Long gameId) {
        String email = RequestUtil.getEmail(httpServletRequest);
        return ResponseEntity.ok(gameCommentService.getThreadedComments(gameId));
    }

    //Like comment
    @PutMapping("/comments/{commentId}/like")
    public ResponseEntity<?> likeComment (HttpServletRequest httpServletRequest,
                                                        @PathVariable Long commentId) {
        String email = RequestUtil.getEmail(httpServletRequest);
        gameCommentService.likeComment(commentId);
        return ResponseEntity.ok("Liked");
    }

    @PostMapping("/{gameId}/assign-team")
    public ResponseEntity<String> assignTeam(HttpServletRequest httpServletRequest,
                                                      @PathVariable Long gameId,
                                                      @RequestBody AssignTeamRequest request) {

        String email = RequestUtil.getEmail(httpServletRequest);
        gameService.assignTeam(gameId, request);
        return ResponseEntity.ok("Team assigned successfully");
    }

    @PutMapping("/{gameId}")
    public ResponseEntity<String> updateGame(HttpServletRequest httpServletRequest,
                                             @PathVariable Long gameId,
                                             @RequestBody GameUpdateRequest request) {
        String email = RequestUtil.getEmail(httpServletRequest);
        gameService.updateGame(gameId, request, email);
        return ResponseEntity.ok("Game updated successfully.");
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<String> deleteGame(HttpServletRequest httpServletRequest,
                                             @PathVariable Long gameId) {
        String email = RequestUtil.getEmail(httpServletRequest);
        gameService.cancelGame(gameId, email);
        return ResponseEntity.ok("Game cancelled successfully.");
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<String> updateComment(HttpServletRequest httpServletRequest,
                                             @PathVariable Long commentId,
                                             @RequestBody String content) {
        String email = RequestUtil.getEmail(httpServletRequest);
        gameCommentService.editComment(commentId, content, email);
        return ResponseEntity.ok("Comments updated successfully.");
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(HttpServletRequest httpServletRequest,
                                                @PathVariable Long commentId) {
        String email = RequestUtil.getEmail(httpServletRequest);
        gameCommentService.deleteComment(commentId, email);
        return ResponseEntity.ok("Comments deleted successfully.");
    }
}
