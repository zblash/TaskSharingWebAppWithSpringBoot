package com.tasksharing.tasksharing.services.Concrete;

import com.tasksharing.tasksharing.models.Group;
import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.repositories.UserRepository;
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

    @Autowired
    private SecurityService securityService;

    public List<User> findAll() {

        return userRepository.findAll();
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public User findById(Long userId){
        Optional<User> group = userRepository.findById(userId);

        try{
            if (!group.isPresent())
                throw new RuntimeException();

            return group.get();

        }catch (Exception ex){
            throw new RuntimeException();
        }

    }

    public void Add(User user){

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

    }

    public void Delete(User user){
        userRepository.delete(user);
    }

    public User Update(User user){
        return userRepository.saveAndFlush(user);
    }

    public List<User> findByGroups_Id(Long id){
        return userRepository.findByGroups_Id(id);
    }

    public boolean LoggedInUserhasGroup(Group group){

        User user = userRepository.findByUserName(securityService.findLoggedInUsername());
        Optional<List<User>> users = Optional.ofNullable(userRepository.findByGroups_Id(group.getId()));
        return users.isPresent() && users.get().contains(user);
    }
}

