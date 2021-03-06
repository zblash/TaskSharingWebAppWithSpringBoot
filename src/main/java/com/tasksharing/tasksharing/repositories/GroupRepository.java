package com.tasksharing.tasksharing.repositories;

import com.tasksharing.tasksharing.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface GroupRepository extends JpaRepository<Group,Long> {
    List<Group> findByUsers_Id(Long users_id);

    Group findBySlugName(String slugName);
}
