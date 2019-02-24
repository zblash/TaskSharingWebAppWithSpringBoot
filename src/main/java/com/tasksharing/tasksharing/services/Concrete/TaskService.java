package com.tasksharing.tasksharing.services.Concrete;

import com.tasksharing.tasksharing.models.Task;
import com.tasksharing.tasksharing.repositories.GroupRepository;
import com.tasksharing.tasksharing.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private GroupRepository groupRepository;

    public List<Task> findAll() {

        return taskRepository.findAll();
    }

    public Task findById(Long taskId){
        Optional<Task> task = taskRepository.findById(taskId);

        try{
            if (!task.isPresent())
                throw new RuntimeException();

            return task.get();

        }catch (Exception ex){
            throw new RuntimeException();
        }

    }

    public void Add(Task task){
//        task.setGroup(groupRepository.findById(task.getGroupId()).orElseThrow(RuntimeException::new));
        taskRepository.save(task);
    }

    public void Delete(Task task){
        taskRepository.delete(task);
    }

    public Task Update(Task task){
       return taskRepository.saveAndFlush(task);
    }
}
