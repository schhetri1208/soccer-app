package com.soccerapp.repository;

import com.soccerapp.model.GameParticipant;
import com.soccerapp.model.GameParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameParticipantRepository extends JpaRepository<GameParticipant, GameParticipantId> {

    List<GameParticipant> findByGameId(Long gameId);
}
