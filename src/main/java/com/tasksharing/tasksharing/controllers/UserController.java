package com.tasksharing.tasksharing.controllers;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SecurityService securityService;


    @GetMapping("/register")
    public String Register(Model model){
        model.addAttribute("user",new User());
        return "register";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){
        model.addAttribute("user", user);

        if (result.hasErrors()){
            return "register";
        } else {
            userService.Add(user);

        }
        return "redirect:/login";
    }

    @GetMapping("/me")
    public String Me(Authentication authentication,Model model){
        model.addAttribute("user",userService.findByUserName(authentication.getName()));
        return "me/index";
    }

    @GetMapping("me/groups")
    public String meGroups(Model model){
        securityService.reloadLoggedInUser();
        model.addAttribute("groups",userService.findById(securityService.findLoggedInUser().getId()).getGroups());
        return "me/groups";
    }

    @RequestMapping("/login")
    public String loginPage(){
        return "login";
    }
}
