package com.soccerapp.service.dto;

import java.time.LocalDateTime;
import java.util.List;

public class GameCommentResponse {
    private Long id;
    private String author;
    private String content;
    private Integer likes;
    private LocalDateTime createdAt = LocalDateTime.now();
    private List<GameCommentResponse> replies;

}
