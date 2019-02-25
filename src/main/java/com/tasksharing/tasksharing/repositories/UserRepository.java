package com.tasksharing.tasksharing.repositories;

import com.tasksharing.tasksharing.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findByGroups_Id(Long groups_id);

    User findByUserName(String userName);
}
