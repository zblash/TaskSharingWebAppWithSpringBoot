package com.tasksharing.tasksharing.repositories;

import com.tasksharing.tasksharing.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String resetToken);
    User findByUserName(String userName);
}
