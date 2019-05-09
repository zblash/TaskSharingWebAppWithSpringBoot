package com.tasksharing.tasksharing.controllers;

import com.tasksharing.tasksharing.models.Task;
import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.models.UserTaskModel;
import com.tasksharing.tasksharing.services.Concrete.GroupService;
import com.tasksharing.tasksharing.services.Concrete.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    GroupService groupService;

    Logger logger = LoggerFactory.getLogger(TaskController.class);

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/task/add-task/{slugname}")
    public ResponseEntity<?> addTask(@PathVariable("slugname") String slugname, @RequestBody Task newtask) {
        newtask.setGroup(groupService.findBySlugName(slugname));
        newtask.setActive(true);
        taskService.Add(newtask);
        return ResponseEntity.ok(taskService.Add(newtask));
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @DeleteMapping("/task/delete-to/{slugname}")
    public ResponseEntity<?> deleteTask(@PathVariable String slugname, @RequestParam Long id) {
        taskService.Delete(taskService.findById(id));
        return ResponseEntity.ok().body("Task deleted");
    }


    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/task/update/{slugname}")
    public ResponseEntity<?> updateTaskPost(@PathVariable String slugname, @RequestParam Long id, @Valid @RequestBody Task task) {

        Task taskByid = taskService.findById(id);
        if (taskService.hasGroup(taskByid, slugname)) {
            taskByid.setName(task.getName());
            taskByid.setDescription(task.getDescription());
            return ResponseEntity.ok(taskService.Add(taskByid));

        }
            return ResponseEntity.badRequest().body("Task not found");


    }


    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/task/assigntask/randomly/{slugname}")
    public ResponseEntity<?> assignTaskRandomlyPost(@PathVariable String slugname, @Valid @RequestBody UserTaskModel taskModel) {

        taskModel.getTasks().forEach(task -> {
            User u = taskModel.getUsers().stream()
                    .skip((int) (taskModel.getTasks().size() * Math.random() + 1))
                    .findFirst()
                    .get();
            task.addUser(u);
            task.setActive(false);
            taskService.Update(task);
            taskModel.getUsers().remove(u);
        });
        return ResponseEntity.ok(taskModel);
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/task/assigntask/{slugname}")
    public ResponseEntity<?> assignTaskPost(@PathVariable String slugname,@RequestParam Long id, @Valid @RequestBody UserTaskModel taskModel) {

        Task task = taskService.findById(id);

            taskModel.getUsers().forEach(task::addUser);
            task.setActive(false);
            return ResponseEntity.ok(taskService.Update(task));

    }
}


