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
        return task.orElseThrow(RuntimeException::new);

    }

    public void Add(Task task){
        taskRepository.save(task);
    }

    public void Delete(Task task){
        taskRepository.delete(task);
    }

    public Task Update(Task task){
       return taskRepository.saveAndFlush(task);
    }

    public boolean hasGroup(Task task, String slugname) {
       return task.getGroup().getSlugName().equals(slugname);
    }
}
