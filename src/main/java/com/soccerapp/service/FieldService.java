package com.soccerapp.service;

import com.soccerapp.model.FieldLocation;
import com.soccerapp.service.dto.CreateFieldRequest;
import com.soccerapp.service.dto.FieldResponse;
import org.springframework.stereotype.Service;
import com.soccerapp.repository.FieldLocationRepository;

import java.util.List;

@Service
public class FieldService {
    private final FieldLocationRepository fieldLocationRepository;

    public FieldService(FieldLocationRepository fieldLocationRepository) {
        this.fieldLocationRepository = fieldLocationRepository;
    }

    public void addField(CreateFieldRequest request) {
        FieldLocation location = new FieldLocation();
        location.setName(request.name());
        location.setLatitude(request.latitude());
        location.setLongitude(request.longitude());
        location.setCity(request.city());
        location.setState(request.state());
        location.setZip(request.zip());

        fieldLocationRepository.save(location);
    }

    public List<FieldResponse> getFieldsByCity(String city){
        return fieldLocationRepository.getFieldsByCityIgnoreCase(city)
                .stream()
                .map(f -> new FieldResponse(f.getId(), f.getName(), f.getLatitude(), f.getLongitude()))
                .toList();
    }
}
