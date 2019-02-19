package com.tasksharing.tasksharing.services.Concrete;

import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {

        return userRepository.findAll();
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public User findById(Long taskId){
        Optional<User> group = userRepository.findById(taskId);

        try{
            if (!group.isPresent())
                throw new RuntimeException();

            return group.get();

        }catch (Exception ex){
            throw new RuntimeException();
        }

    }

    public void Add(User task){
        userRepository.save(task);
    }

    public void Delete(User task){
        userRepository.delete(task);
    }

    public User Update(User task){
        return userRepository.saveAndFlush(task);
    }

    public List<User> findByGroups_Id(Long id){
        return userRepository.findByGroups_Id(id);
    }
}

