package com.soccerapp.service;

import com.soccerapp.model.Game;
import com.soccerapp.model.GameComment;
import com.soccerapp.model.User;
import com.soccerapp.repository.GameCommentRepository;
import com.soccerapp.repository.GameRepository;
import com.soccerapp.repository.UserRepository;
import com.soccerapp.service.dto.CommentResponse;
import com.soccerapp.service.dto.GameResponse;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameCommentService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameCommentRepository gameCommentRepository;

    public GameCommentService(UserRepository userRepository, GameRepository gameRepository, GameCommentRepository gameCommentRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.gameCommentRepository = gameCommentRepository;
    }

    public void addComment(Long gameId, String content, Long parentId, String email) {
        User user = userRepository.findByEmail(email);
        Game game = gameRepository.findById(gameId).orElseThrow();
        GameComment parent = parentId != null ? gameCommentRepository.findById(parentId).orElse(null) : null;

        GameComment comment = new GameComment();
        comment.setGame(game);
        comment.setUser(user);
        comment.setContent(content);
        comment.setParentComment(parent);
        System.out.println("Replying to comment ID: " + parentId);

        gameCommentRepository.save(comment);
    }

    public void likeComment(Long commentId) {
        GameComment comment = gameCommentRepository.findById(commentId).orElseThrow();
        comment.setLikes(comment.getLikes()+1);
        gameCommentRepository.save(comment);
    }

    public List<CommentResponse> getThreadedComments(Long gameId) {
        List<GameComment> allComments = gameCommentRepository.findByGameId(gameId);
        Map<Long, CommentResponse> map = new HashMap<>();
        List<CommentResponse> topLevelComment = new ArrayList<>();

        for (GameComment c: allComments) {
            CommentResponse dto = new CommentResponse();
            dto.setId(c.getId());
            dto.setAuthor(c.getUser().getFirstName());
            dto.setContent(c.getContent());
            dto.setLikes(c.getLikes());
            dto.setCreatedAt(c.getCreatedAt());
            map.put(c.getId(), dto);

            if (c.getParentComment() == null) {
                topLevelComment.add(dto);
            }
            else {
                map.get(c.getParentComment().getId()).getReplies().add(dto);
            }
        }
        return topLevelComment;
    }

    public void editComment(Long commentId, String content, String userEmail) {
        GameComment comment = gameCommentRepository.findById(commentId).orElseThrow();
        if(!comment.getUser().getEmail().equals(userEmail)){
            throw new AccessDeniedException("You can only edit your own comments.");
        }
        comment.setContent(content);
        gameCommentRepository.save(comment);
    }

    public void deleteComment(Long commentId, String userEmail) {
        GameComment comment = gameCommentRepository.findById(commentId).orElseThrow();
        if(!comment.getUser().getEmail().equals(userEmail)){
            throw new AccessDeniedException("You can only delete your own comments.");
        }
        gameCommentRepository.delete(comment);
    }
}
