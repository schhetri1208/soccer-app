package com.soccerapp.repository;

import com.soccerapp.model.FieldLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldLocationRepository extends JpaRepository<FieldLocation, Long> {
    List<FieldLocation> getFieldsByCityIgnoreCase(String city);
}
