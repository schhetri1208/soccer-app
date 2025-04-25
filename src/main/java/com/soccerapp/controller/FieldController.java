package com.soccerapp.controller;

import com.soccerapp.security.JwtUtil;
import com.soccerapp.service.FieldService;
import com.soccerapp.service.dto.CreateFieldRequest;
import com.soccerapp.service.dto.FieldResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/fields")
@Controller
public class FieldController {

    private final FieldService fieldService;
    private final JwtUtil jwtUtil;

    public FieldController(FieldService fieldService, JwtUtil jwtUtil) {
        this.fieldService = fieldService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<String> addField(@RequestHeader("Authorization") String token,
                                           @RequestBody CreateFieldRequest request) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        fieldService.addField(request);
        return ResponseEntity.ok("Field added successfully");
    }

    @GetMapping("/city/{city}")
    public ResponseEntity <List<FieldResponse>> getFieldsByCity(@RequestHeader("Authorization") String token,
                                                                @PathVariable String city) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        return ResponseEntity.ok(fieldService.getFieldsByCity(city));
    }
}
