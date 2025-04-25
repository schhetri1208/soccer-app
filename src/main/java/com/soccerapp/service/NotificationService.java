package com.soccerapp.service;

import com.soccerapp.model.Group;
import java.util.List;
import com.soccerapp.model.GroupMember;
import com.soccerapp.model.User;
import com.soccerapp.repository.GroupMemberRepository;
import com.soccerapp.repository.GroupRepository;
import com.soccerapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final GroupMemberRepository groupMemberRepository;

    public NotificationService(UserRepository userRepository, GroupRepository groupRepository, GroupMemberRepository groupMemberRepository) {
        this.groupMemberRepository = groupMemberRepository;
    }

    //For now just logging the notification in console later, we will add push noitification and trigger email
    public void sendNotification (Long groupId, String message) {
        List <GroupMember> members = groupMemberRepository.findByGroupId(groupId);

        for (GroupMember gm : members) {
            User user = gm.getUser();
            System.out.println("🔔 Notification to " + user.getEmail() + ": " + message);
        }
    }
}
