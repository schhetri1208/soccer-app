package com.soccerapp.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Long id;
    private String author;
    private String content;
    private Integer likes;
    private LocalDateTime createdAt;
    private List<CommentResponse> replies = new ArrayList<>();

}
