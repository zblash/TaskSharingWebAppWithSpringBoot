package com.tasksharing.tasksharing.services.Concrete;

import com.tasksharing.tasksharing.models.Group;
import com.tasksharing.tasksharing.models.Task;
import com.tasksharing.tasksharing.repositories.TaskRepository;
import com.tasksharing.tasksharing.services.Abstract.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService {

    @Autowired
    private TaskRepository taskRepository;


    @Override
    public List<Task> findAll() {

        return taskRepository.findAll();
    }

    @Override
    public Task findById(Long taskId){
        Optional<Task> task = taskRepository.findById(taskId);
        return task.orElseThrow(RuntimeException::new);

    }

    @Override
    public Task Add(Task task){
        return taskRepository.save(task);
    }

    @Override
    public void Delete(Task task){
        taskRepository.delete(task);
    }

    @Override
    public Task Update(Task task){
       return taskRepository.saveAndFlush(task);
    }

    @Override
    public boolean hasGroup(Task task, String slugname) {
       return task.getGroup().getSlugName().equals(slugname);
    }

    @Override
    public List<Task> findByGroup(Group group) {
        return taskRepository.findAllByGroup_Id(group.getId());
    }
}
