package com.tasksharing.tasksharing.services.Concrete;

import com.tasksharing.tasksharing.models.Group;
import com.tasksharing.tasksharing.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public List<Group> findAll() {

        return groupRepository.findAll();
    }

    public Group findById(Long taskId){
        Optional<Group> group = groupRepository.findById(taskId);

        try{
            if (!group.isPresent())
                throw new RuntimeException();

            return group.get();

        }catch (Exception ex){
            throw new RuntimeException();
        }

    }

    public void Add(Group task){
        groupRepository.save(task);
    }

    public void Delete(Group task){
        groupRepository.delete(task);
    }

    public Group Update(Group task){
        return groupRepository.saveAndFlush(task);
    }

    public Group findBySlugName(String name) {
        return groupRepository.findBySlugName(name);
    }
}
