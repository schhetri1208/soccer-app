package com.soccerapp.repository;

import com.soccerapp.model.Group;
import com.soccerapp.model.GroupMemberId;
import com.soccerapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.soccerapp.model.GroupMember;
import java.util.List;


@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {
    List<GroupMember> findByUser(User user);
    List<GroupMember> findByGroup(Group group);

    List<GroupMember> findByGroupId(Long groupId);
}
