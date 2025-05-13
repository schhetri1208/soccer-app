package com.soccerapp.controller;

import com.soccerapp.model.GameParticipant;
import com.soccerapp.model.Group;
import com.soccerapp.security.JwtUtil;
import com.soccerapp.service.GroupService;
import com.soccerapp.service.dto.AddMemberRequest;
import com.soccerapp.service.dto.AssignTeamRequest;
import com.soccerapp.service.dto.CreateGroupRequest;
import com.soccerapp.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    private final JwtUtil jwtUtil;

    public GroupController(GroupService groupService, JwtUtil jwtUtil) {
        this.groupService = groupService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(HttpServletRequest httpServletRequest,
                                             @RequestBody CreateGroupRequest request) {
        String email = RequestUtil.getEmail(httpServletRequest);
        return ResponseEntity.ok(groupService.createGroup(request,email));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Group>> getUserGroups(HttpServletRequest httpServletRequest) {
        String email = RequestUtil.getEmail(httpServletRequest);
        return ResponseEntity.ok(groupService.getGroupForUser(email));
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<String> addMemberToGroup(HttpServletRequest httpServletRequest,
                                                  @RequestBody AddMemberRequest request,
                                                  @PathVariable Long groupId) {
        String email = RequestUtil.getEmail(httpServletRequest);
        groupService.addMemberToGroup(groupId, request, email);
        return ResponseEntity.ok("User added to the group successfully");
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<String>> getAllMembersOfGroup(HttpServletRequest httpServletRequest,
                                                             @PathVariable Long groupId) {
        String email = RequestUtil.getEmail(httpServletRequest);
        return ResponseEntity.ok(groupService.getAllMembersOfGroup(groupId));
    }

}
