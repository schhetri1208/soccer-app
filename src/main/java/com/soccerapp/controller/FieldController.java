package com.soccerapp.controller;

import com.soccerapp.security.JwtUtil;
import com.soccerapp.service.FieldService;
import com.soccerapp.service.dto.CreateFieldRequest;
import com.soccerapp.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<String> addField(HttpServletRequest httpServletRequest,
                                           @RequestBody CreateFieldRequest request) {
        String email = RequestUtil.getEmail(httpServletRequest);
        fieldService.addField(request);
        return ResponseEntity.ok("Field added successfully");
    }

    @GetMapping("/city/{city}")
    public ResponseEntity <List<FieldResponse>> getFieldsByCity(HttpServletRequest httpServletRequest,
                                                                @PathVariable String city) {
        String email = RequestUtil.getEmail(httpServletRequest);
        return ResponseEntity.ok(fieldService.getFieldsByCity(city));
    }
}
