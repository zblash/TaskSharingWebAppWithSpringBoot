package com.tasksharing.tasksharing.controllers;

import com.tasksharing.tasksharing.models.CustomPrincipal;
import com.tasksharing.tasksharing.models.Group;
import com.tasksharing.tasksharing.models.Privilege;
import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.repositories.PrivilegeRepository;
import com.tasksharing.tasksharing.services.Concrete.GroupService;
import com.tasksharing.tasksharing.services.Concrete.SecurityService;
import com.tasksharing.tasksharing.services.Concrete.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sun.rmi.runtime.Log;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

@Controller
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    UserService userService;

    @Autowired
    SecurityService securityService;

    @Autowired
    PrivilegeRepository privilegeRepository;

    private final Logger logger = LoggerFactory.getLogger(GroupController.class);


    @PreAuthorize("isMember(#slugname)")
    @GetMapping("/group/{slugname}")
    public String Group(@PathVariable("slugname") String slugname, Model model){

        Group group = groupService.findBySlugName(slugname);
            model.addAttribute("group",group);
            return "group/index";
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#name,'ADMIN')")
    @GetMapping("/group/add-user/{name}")
    public String AddUser(@PathVariable("name") String name, Model model){
        model.addAttribute("groupName",name);
        return "group/adduser";
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#name,'ADMIN')")
    @PostMapping("/group/add-user/{name}")
    public String AddUser(@PathVariable("name") String name,@RequestParam("username") String username, Model model){
        logger.info(name,username);
        User user = userService.findByUserName(username);
        logger.info(user.getUserName());
        Group group = groupService.findBySlugName(name);
        Privilege createdprivilege = privilegeRepository.findByName(group.getGroupName().toUpperCase()+"_USER");
        if(createdprivilege == null){
            createdprivilege = new Privilege();
            createdprivilege.setName(group.getGroupName().toUpperCase()+"_USER");
        }

        user.addPrivilege(createdprivilege);
        group.addUser(user);
        logger.info(user.getUserName());
        userService.Update(user);
        groupService.Update(group);
        return "redirect:/group/"+group.getGroupName();
    }

    @GetMapping("/group/create")
    public String CreateGroup(Model model){
        model.addAttribute("group",new Group());
        return "group/create";
    }

    @PostMapping("/group/create")
    public String CreateGroupPost(@Valid @ModelAttribute("group") Group group,Authentication authentication, BindingResult result, Model model){

        if (result.hasErrors()){
            return "group/create";
        }
        groupService.Add(group);
        User user = userService.findByUserName(securityService.findLoggedInUsername());
        user.addGroup(group);
        Privilege createdprivilege = new Privilege();
        createdprivilege.setName(group.getGroupName().toUpperCase()+"_ADMIN");
        user.addPrivilege(createdprivilege);
        userService.Update(user);
        ((CustomPrincipal) authentication.getPrincipal()).setUser(user);

        securityService.reloadPrivilege(user);
        return "redirect:/group/"+group.getSlugName();
    }


}
