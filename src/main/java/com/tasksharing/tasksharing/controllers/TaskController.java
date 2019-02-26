package com.tasksharing.tasksharing.controllers;

import com.tasksharing.tasksharing.models.Group;
import com.tasksharing.tasksharing.models.Task;
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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    GroupService groupService;

    Logger logger = LoggerFactory.getLogger(TaskController.class);

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/task/add-task/{slugname}")
    public String addTask(@PathVariable("slugname") String slugname, @Valid Task newtask, BindingResult result,final RedirectAttributes redirectAttributes){

        if(result.hasErrors()){
            logger.info(String.valueOf(result.getErrorCount()));
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newtask", result);
            redirectAttributes.addFlashAttribute("newtask", newtask);
        }else {
            newtask.setGroup(groupService.findBySlugName(slugname));
            taskService.Add(newtask);
        }

        return "redirect:/group/"+slugname;
        }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @DeleteMapping("/task/delete-to/{slugname}")
    public String deleteTask(@PathVariable String slugname,@RequestParam Long id){
        taskService.Delete(taskService.findById(id));
        return "redirect:/group/"+slugname;
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @GetMapping("/task/update/{slugname}")
    public String updateTask(@PathVariable String slugname, @RequestParam Long id, Model model){
        Task task = taskService.findById(id);
        if(taskService.hasGroup(task,slugname)){
            model.addAttribute("task",task);
            return "/task/edit";
        }
        return "redirect:/group/"+slugname;
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/task/update/{slugname}")
    public String updateTaskPost(@PathVariable String slugname,@RequestParam Long id,@Valid Task task, BindingResult result,final RedirectAttributes redirectAttributes,Model model){

        if(result.hasErrors()){
            model.addAttribute("task",task);
            return "/task/edit";
        }
            Task taskByid = taskService.findById(id);
            if(taskService.hasGroup(taskByid,slugname)){
                taskByid.setName(task.getName());
                taskByid.setDescription(task.getDescription());
                taskService.Add(taskByid);
            }
        return "redirect:/group/"+slugname;
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/task/assigntask/randomly/{slugname}")
    public String assignTaskRandom(){
        return "";
    }
}


