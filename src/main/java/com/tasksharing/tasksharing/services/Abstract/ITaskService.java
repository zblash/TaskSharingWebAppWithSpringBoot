package com.tasksharing.tasksharing.services.Abstract;

import com.tasksharing.tasksharing.models.Task;

import java.util.List;
import java.util.Optional;

public interface ITaskService {

    List<Task> findAll();

    Task findById(Long taskId);

    void Add(Task task);

    void Delete(Task task);

    Task Update(Task task);

    boolean hasGroup(Task task, String slugname);
}
