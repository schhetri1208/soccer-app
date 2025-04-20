package com.soccerapp.service;

import com.soccerapp.model.Group;
import com.soccerapp.model.GroupMemberId;
import com.soccerapp.model.User;
import com.soccerapp.model.GroupMember;
import com.soccerapp.repository.GroupMemberRepository;
import com.soccerapp.repository.GroupRepository;
import com.soccerapp.repository.UserRepository;
import com.soccerapp.service.dto.AddMemberRequest;
import com.soccerapp.service.dto.CreateGroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    public GroupService(GroupRepository groupRepository, UserRepository userRepository, GroupMemberRepository groupMemberRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    private GroupRepository groupRepository;
    private UserRepository userRepository;
    private GroupMemberRepository groupMemberRepository;

    public Group createGroup(CreateGroupRequest request, String email) {
        User creator = userRepository.findByEmail(email);
        if (creator == null){
            throw new RuntimeException("User not found");
        }
        Group group = new Group();
        group.setName(request.name());
        group.setLocation(request.location());
        group.setCreatedBy(creator);

        groupRepository.save(group);

        GroupMember groupMember = new GroupMember();
        groupMember.setId(new GroupMemberId(group.getId(), creator.getId()));
        groupMember.setGroup(group);
        groupMember.setUser(creator);
        groupMember.setRole("admin");
        groupMemberRepository.save(groupMember);

        return group;
    }

    public List<Group> getGroupForUser(String email) {
        User user = userRepository.findByEmail(email);
        List<GroupMember> members = groupMemberRepository.findByUser(user);
        return members.stream()
                .map(GroupMember::getGroup)
                .toList();
    }

    public void addMemberToGroup(Long groupId, AddMemberRequest request, String addedByEmail) {
        Group group = groupRepository.findById(groupId).orElseThrow();
        User user = userRepository.findByEmail(addedByEmail);

        GroupMemberId adminId = new GroupMemberId(group.getId(), user.getId());
        GroupMember adminMember = groupMemberRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Not a group member"));

        if (!"admin".equals(adminMember.getRole())) {
            throw new RuntimeException("Only admins can add new group member");
        }

        User newUser = userRepository.findByEmail(request.email());
        if (newUser == null) {
            throw new RuntimeException("User not found: " + request.email());
        }
        GroupMember newMember = new GroupMember();
        newMember.setId(new GroupMemberId(groupId, newUser.getId()));
        newMember.setGroup(group);
        newMember.setUser(newUser);
        newMember.setRole(request.role());

        groupMemberRepository.save(newMember);
    }

    public List<String> getAllMembersOfGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow();
        return groupMemberRepository.findByGroup(group).stream()
                .map(member -> member.getUser().getEmail() + "(" + member.getRole() + ")")
                .toList();
    }
}
