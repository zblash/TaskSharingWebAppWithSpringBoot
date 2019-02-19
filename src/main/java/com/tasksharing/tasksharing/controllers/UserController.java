package com.tasksharing.tasksharing.controllers;

import com.tasksharing.tasksharing.services.Concrete.GroupService;
import com.tasksharing.tasksharing.services.Concrete.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;



    @GetMapping("/me")
    public String Me(Authentication authentication,Model model){
        model.addAttribute("user",userService.findByUserName(authentication.getName()));

        return "me/index";
    }

    @GetMapping("me/groups")
    public String meGroups(Authentication authentication,Model model){
        model.addAttribute("groups",userService.findByUserName(authentication.getName()).getGroups());
        return "me/groups";
    }

    @RequestMapping("/login")
    public String loginPage(){
        return "login";
    }
}
