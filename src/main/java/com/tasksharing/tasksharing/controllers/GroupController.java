package com.tasksharing.tasksharing.controllers;

import com.tasksharing.tasksharing.models.*;
import com.tasksharing.tasksharing.repositories.PrivilegeRepository;
import com.tasksharing.tasksharing.services.Concrete.GroupService;
import com.tasksharing.tasksharing.services.Concrete.SecurityService;
import com.tasksharing.tasksharing.services.Concrete.TaskService;
import com.tasksharing.tasksharing.services.Concrete.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    SecurityService securityService;

    @Autowired
    PrivilegeRepository privilegeRepository;

    private final Logger logger = LoggerFactory.getLogger(GroupController.class);


    @PreAuthorize("isMember(#slugname)")
    @GetMapping("/group/{slugname}")
    public String Group(@PathVariable("slugname") String slugname, Model model){

            securityService.reloadLoggedInUser();
            model.addAttribute("group",groupService.findBySlugName(slugname));
            if (!model.containsAttribute("newtask")){
                model.addAttribute("newtask", new Task());

            }
            return "group/index";
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @GetMapping("/group/update/{slugname}")
    public String updateGroup(@PathVariable String slugname,Model model){
        model.addAttribute("group",groupService.findBySlugName(slugname));
        return "group/edit";
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/group/update/{slugname}")
    public String updateGroupPost(@PathVariable String slugname,@Valid Group group,BindingResult result,Model model){

        if (result.hasErrors()){
            model.addAttribute("group",group);
            return "group/edit";
        }

        Group updateGroup = groupService.findBySlugName(slugname);
        groupService.Update(updateGroup,group);
        securityService.reloadLoggedInUser();
        return "redirect:/group/"+group.getSlugName();
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/group/remove-to/{slugname}")
    public String removeUser(@PathVariable String slugname,@RequestParam Long id){
        User user = userService.findById(id);

        if(userService.hasGroup(user,slugname)){
            userService.removePrivileges(user,slugname);
            Group group = groupService.findBySlugName(slugname);
            group.removeUser(user);
            groupService.Update(group);
        }
        return "redirect:/group/"+slugname;
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#name,'ADMIN')")
    @PostMapping("/group/add-user/{name}")
    public String AddUser(@PathVariable("name") String name,@RequestParam("username") String username, Model model){
        User user = userService.findByUserName(username);
        Group group = groupService.findBySlugName(name);
        Privilege createdprivilege = privilegeRepository.findByName(group.getSlugName().toUpperCase()+"_USER");
        if(createdprivilege == null){
            createdprivilege = new Privilege();
            createdprivilege.setName(group.getSlugName().toUpperCase()+"_USER");
        }

        user.addPrivilege(createdprivilege);
        group.addUser(user);
        userService.Update(user);
        groupService.Update(group);
        return "redirect:/group/"+group.getSlugName();
    }

    @GetMapping("/group/create")
    public String CreateGroup(Model model){
        model.addAttribute("group",new Group());
        return "group/create";
    }

    @PostMapping("/group/create")
    public String CreateGroupPost(@Valid @ModelAttribute("group") Group group,Authentication authentication, BindingResult result, Model model){
        model.addAttribute("group",group);
        if (result.hasErrors()){
            logger.info("sea");
            return "group/create";
        }
        groupService.Add(group);
        User user = userService.findByUserName(securityService.findLoggedInUsername());
        user.addGroup(group);
        Privilege createdprivilege = new Privilege();
        createdprivilege.setName(group.getSlugName().toUpperCase()+"_ADMIN");
        user.addPrivilege(createdprivilege);
        userService.Update(user);
        ((CustomPrincipal) authentication.getPrincipal()).setUser(user);

        securityService.reloadLoggedInUser();
        return "redirect:/me/groups/";
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @GetMapping("/group/delete/{slugname}")
    public String DeleteGroup(@PathVariable String slugname){
        groupService.removeGroup(slugname);

        securityService.reloadLoggedInUser();
        return "redirect:/me/groups";
    }
}
