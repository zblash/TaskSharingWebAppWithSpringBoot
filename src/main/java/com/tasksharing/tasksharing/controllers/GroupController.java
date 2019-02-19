package com.tasksharing.tasksharing.controllers;

import com.tasksharing.tasksharing.models.Group;
import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.services.Concrete.GroupService;
import com.tasksharing.tasksharing.services.Concrete.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.logging.Level;

@Controller
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    UserService userService;

    private final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @GetMapping("/group/{name}")
    public String Group(@PathVariable("name") String name, Authentication authentication, Model model){
        logger.info(name);
       User user = userService.findByUserName(authentication.getName());
        Group group = groupService.findByGroupName(name);

        if(userService.hasGroup(user,group)){
            model.addAttribute("group",group);
            return "group/index";
        }
        return "redirect:/me/groups";
    }
}
