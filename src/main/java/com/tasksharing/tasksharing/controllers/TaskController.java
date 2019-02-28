package com.tasksharing.tasksharing.controllers;

import com.tasksharing.tasksharing.models.Group;
import com.tasksharing.tasksharing.models.Task;
import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.models.UserTaskModel;
import com.tasksharing.tasksharing.services.Concrete.GroupService;
import com.tasksharing.tasksharing.services.Concrete.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;

@Controller
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    GroupService groupService;

    Logger logger = LoggerFactory.getLogger(TaskController.class);

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/task/add-task/{slugname}")
    public String addTask(@PathVariable("slugname") String slugname, @Valid Task newtask, BindingResult result, final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            logger.info(String.valueOf(result.getErrorCount()));
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newtask", result);
            redirectAttributes.addFlashAttribute("newtask", newtask);
        } else {
            newtask.setGroup(groupService.findBySlugName(slugname));
            newtask.setActive(true);
            taskService.Add(newtask);
        }

        return "redirect:/group/" + slugname;
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @DeleteMapping("/task/delete-to/{slugname}")
    public String deleteTask(@PathVariable String slugname, @RequestParam Long id) {
        taskService.Delete(taskService.findById(id));
        return "redirect:/group/" + slugname;
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @GetMapping("/task/update/{slugname}")
    public String updateTask(@PathVariable String slugname, @RequestParam Long id, Model model) {
        Task task = taskService.findById(id);
        if (taskService.hasGroup(task, slugname)) {
            model.addAttribute("task", task);
            return "/task/edit";
        }
        return "redirect:/group/" + slugname;
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/task/update/{slugname}")
    public String updateTaskPost(@PathVariable String slugname, @RequestParam Long id, @Valid Task task, BindingResult result, final RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("task", task);
            return "/task/edit";
        }
        Task taskByid = taskService.findById(id);
        if (taskService.hasGroup(taskByid, slugname)) {
            taskByid.setName(task.getName());
            taskByid.setDescription(task.getDescription());
            taskService.Add(taskByid);
        }
        return "redirect:/group/" + slugname;
    }


    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @GetMapping("/task/assigntask/randomly/{slugname}")
    public String assignTaskRandomly(@PathVariable String slugname, Model model) {
        Group group = groupService.findBySlugName(slugname);
        UserTaskModel taskModel = new UserTaskModel();
        taskModel.setTasks(group.getTasks());
        taskModel.setUsers(group.getUsers());
        model.addAttribute("group", group);
        model.addAttribute("taskModel", taskModel);
        return "/task/assigntaskrandomly";
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/task/assigntask/randomly/{slugname}")
    public String assignTaskRandomlyPost(@PathVariable String slugname, @Valid @ModelAttribute UserTaskModel taskModel, Model model, BindingResult result) {

        if (result.hasErrors()) {
            Group group = groupService.findBySlugName(slugname);
            model.addAttribute("group", group);
            model.addAttribute("taskModel", taskModel);
            return "/task/assigntaskrandomly";
        }
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
        return "redirect:/group/" + slugname;
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @GetMapping("/task/assigntask/{slugname}")
    public String assignTask(@PathVariable String slugname,@RequestParam Long id, Model model) {
        Group group = groupService.findBySlugName(slugname);
        UserTaskModel taskModel = new UserTaskModel();
        taskModel.setUsers(group.getUsers());
        taskModel.setTasks(new HashSet<>(Arrays.asList(taskService.findById(id))));
        model.addAttribute("group", group);
        model.addAttribute("taskModel", taskModel);
        return "/task/assigntask";
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/task/assigntask/{slugname}")
    public String assignTaskPost(@PathVariable String slugname,@RequestParam Long id, @Valid @ModelAttribute UserTaskModel taskModel, Model model, BindingResult result) {
        if (result.hasErrors()) {
            Group group = groupService.findBySlugName(slugname);
            model.addAttribute("group", group);
            model.addAttribute("taskModel", taskModel);
            return "/task/assigntask";
        }
        Task task = taskService.findById(id);

            taskModel.getUsers().forEach(task::addUser);
            task.setActive(false);
            taskService.Update(task);

        return "redirect:/group/" + slugname;
    }
}


