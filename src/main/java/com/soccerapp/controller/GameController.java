package com.soccerapp.controller;

import com.soccerapp.model.Game;
import com.soccerapp.security.JwtUtil;
import com.soccerapp.service.GameService;
import com.soccerapp.service.dto.CreateGameRequest;
import com.soccerapp.service.dto.GameResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/games")
public class GameController {

    private GameService gameService;
    private JwtUtil jwtUtil;

    public GameController(GameService gameService, JwtUtil jwtUtil) {
        this.gameService = gameService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<GameResponse> scheduleGame (@RequestHeader ("Authorization") String token,
                                                      @RequestBody CreateGameRequest request) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));

        return ResponseEntity.ok(gameService.scheduleGame(request, email));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<GameResponse>> getGameForGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(gameService.getGameForGroup(groupId));
    }
}
