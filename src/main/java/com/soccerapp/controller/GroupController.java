package com.soccerapp.controller;

import com.soccerapp.model.Group;
import com.soccerapp.security.JwtUtil;
import com.soccerapp.service.GroupService;
import com.soccerapp.service.dto.AddMemberRequest;
import com.soccerapp.service.dto.CreateGroupRequest;
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
    public ResponseEntity<Group> createGroup(@RequestHeader("Authorization") String token,
                                             @RequestBody CreateGroupRequest request) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        return ResponseEntity.ok(groupService.createGroup(request,email));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Group>> getUserGroups(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        return ResponseEntity.ok(groupService.getGroupForUser(email));
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<String> addMemberToGroup(@RequestHeader("Authorization") String token,
                                                  @RequestBody AddMemberRequest request,
                                                  @PathVariable Long groupId) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        groupService.addMemberToGroup(groupId, request, email);
        return ResponseEntity.ok("User added to the group successfully");
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<String>> getAllMembersOfGroup(@RequestHeader("Authorization") String token,
                                                             @PathVariable Long groupId) {
        String email = jwtUtil.getEmailFromToken(token.substring(7));
        return ResponseEntity.ok(groupService.getAllMembersOfGroup(groupId));
    }
}
