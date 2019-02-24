package com.tasksharing.tasksharing.services.Concrete;

import com.tasksharing.tasksharing.models.Group;
import com.tasksharing.tasksharing.models.Privilege;
import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private SecurityService securityService;

    public List<User> findAll() {

        return userRepository.findAll();
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User findById(Long userId) {
        Optional<User> group = userRepository.findById(userId);

        try {
            if (!group.isPresent())
                throw new RuntimeException();

            return group.get();

        } catch (Exception ex) {
            throw new RuntimeException();
        }

    }

    public void Add(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

    }

    public void Delete(User user) {
        userRepository.delete(user);
    }

    public User Update(User user) {
        return userRepository.saveAndFlush(user);
    }

    public List<User> findByGroups_Id(Long id) {
        return userRepository.findByGroups_Id(id);
    }

    public boolean hasGroup(User user, String groupSlugName) {
        for (Group group : user.getGroups()) {
            if (groupSlugName.equals(group.getSlugName())) {
                return true;
            }
        }
        return false;
    }

    public void removePrivileges(User user, String slugName) {
        for (Privilege privilege : user.getPrivileges()){
            if (privilege.getName().startsWith(slugName.toUpperCase())){
                user.removePrivilege(privilege);
                userRepository.save(user);
            }
        }
    }
}

