package com.tasksharing.tasksharing.services.Abstract;

import com.tasksharing.tasksharing.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> findAll();

    User findByUserName(String userName);

    User findById(Long userId);

    void Add(User user);

    void Delete(User user);

    User Update(User user);

    boolean hasGroup(User user, String groupSlugName);

    void removePrivileges(User user, String slugName);

    User findByEmail(String email);

    Optional<User> findByResetToken(String token);
}
