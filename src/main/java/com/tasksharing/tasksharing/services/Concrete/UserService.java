package com.tasksharing.tasksharing.services.Concrete;

import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<User> findAll() {

        return userRepository.findAll();
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(RuntimeException::new);
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

    public boolean hasGroup(User user, String groupSlugName) {
            return user.getGroups().stream().anyMatch(group -> groupSlugName.equals(group.getSlugName()));
    }

    public void removePrivileges(User user, String slugName) {
        user.getPrivileges().forEach(privilege -> {
            if (privilege.getName().startsWith(slugName.toUpperCase())){
                user.removePrivilege(privilege);
                userRepository.save(user);
            }
        });
    }
    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }

    public Optional<User> findByResetToken(String token){
        return userRepository.findByResetToken(token);
    }
}

