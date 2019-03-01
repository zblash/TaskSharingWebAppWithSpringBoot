package com.tasksharing.tasksharing.services.Concrete;

import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.repositories.UserRepository;
import com.tasksharing.tasksharing.services.Abstract.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {

        return userRepository.findAll();
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(RuntimeException::new);
    }

    @Override
    public void Add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

    }

    @Override
    public void Delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public User Update(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public boolean hasGroup(User user, String groupSlugName) {
            return user.getGroups().stream().anyMatch(group -> groupSlugName.equals(group.getSlugName()));
    }

    @Override
    public void removePrivileges(User user, String slugName) {
        user.getPrivileges().forEach(privilege -> {
            if (privilege.getName().startsWith(slugName.toUpperCase())){
                user.removePrivilege(privilege);
                userRepository.save(user);
            }
        });
    }

    @Override
    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }

    @Override
    public Optional<User> findByResetToken(String token){
        return userRepository.findByResetToken(token);
    }
}

