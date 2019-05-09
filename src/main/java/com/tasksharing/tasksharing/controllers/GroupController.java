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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
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
    public ResponseEntity<?> Group(@PathVariable("slugname") String slugname,Authentication authentication){
        authentication.getAuthorities().forEach(grantedAuth -> {
                    logger.info(grantedAuth.getAuthority());
                }
        );
        return ResponseEntity.ok(groupService.findBySlugName(slugname));
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/group/update/{slugname}")
    public ResponseEntity<?> updateGroup(@PathVariable String slugname,@Valid @RequestBody Group group){

        Group updateGroup = groupService.findBySlugName(slugname);
        return ResponseEntity.ok(groupService.Update(updateGroup,group));
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @PostMapping("/group/delete/{slugname}")
    public ResponseEntity<?> removeUser(@PathVariable String slugname,@RequestParam Long id){
        User user = userService.findById(id);

        if(userService.hasGroup(user,slugname)){
            userService.removePrivileges(user,slugname);
            Group group = groupService.findBySlugName(slugname);
            group.removeUser(user);
            return ResponseEntity.ok(groupService.Update(group));
        }
        return ResponseEntity.badRequest().body("User not found");
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#name,'ADMIN')")
    @PostMapping("/group/add-user/{name}")
    public ResponseEntity<?> AddUser(@PathVariable("name") String name,@RequestParam("username") String username){
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
        return ResponseEntity.ok().body("User added to group");
    }

    @PostMapping("/group/create")
    public ResponseEntity<?> CreateGroup(@Valid @RequestBody Group group,Authentication authentication){
        groupService.Add(group);
        User user = userService.findByUserName(securityService.findLoggedInUsername());
        user.addGroup(group);
        Privilege createdprivilege = new Privilege();
        createdprivilege.setName(group.getSlugName().toUpperCase()+"_ADMIN");
        user.addPrivilege(createdprivilege);
        userService.Update(user);
        ((CustomPrincipal) authentication.getPrincipal()).setUser(user);
        return ResponseEntity.ok(group);
    }

    @PreAuthorize("hasPermission('com.tasksharing.tasksharing.models.Group',#slugname,'ADMIN')")
    @GetMapping("/group/delete/{slugname}")
    public ResponseEntity<?> DeleteGroup(@PathVariable String slugname){
        groupService.removeGroup(slugname);

        return ResponseEntity.ok().body("Group removed");
    }
}
