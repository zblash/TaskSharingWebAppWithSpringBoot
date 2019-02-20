package com.tasksharing.tasksharing.controllers;

import com.tasksharing.tasksharing.models.Group;
import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.services.Concrete.GroupService;
import com.tasksharing.tasksharing.services.Concrete.SecurityService;
import com.tasksharing.tasksharing.services.Concrete.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.logging.Level;

@Controller
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    UserService userService;

    @Autowired
    private SecurityService securityService;

    private final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @GetMapping("/group/{name}")
    public String Group(@PathVariable("name") String name,  Model model){
        logger.info(name);


        Group group = groupService.findByGroupName(name);
        if(userService.LoggedInUserhasGroup(group)){
            model.addAttribute("group",group);
            return "group/index";
        }
        return "redirect:/me/groups";
    }

    @GetMapping("/group/create")
    public String CreateGroup(Model model){
        model.addAttribute("group",new Group());
        return "group/create";
    }

    @PostMapping("/group/create")
    public String CreateGroupPost(@Valid @ModelAttribute("group") Group group, BindingResult result, Model model){

        if (result.hasErrors()){
            return "group/create";
        }
        group.addUser(userService.findByUserName(securityService.findLoggedInUsername()));
        groupService.Add(group);
        return "redirect:/group/"+group.getGroupName();
    }


}
