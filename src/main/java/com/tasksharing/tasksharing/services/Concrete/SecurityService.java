package com.tasksharing.tasksharing.services.Concrete;

import com.tasksharing.tasksharing.models.CustomPrincipal;
import com.tasksharing.tasksharing.models.Privilege;
import com.tasksharing.tasksharing.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityService {

    @Autowired
    private UserService userService;

    public String findLoggedInUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth != null ? auth.getName() : null;
    }

    public User findLoggedInUser() {
        User user = ((CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        return user;
    }

    public void reloadLoggedInUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findById(((CustomPrincipal)auth.getPrincipal()).getUser().getId());
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getPrivileges().forEach(privilege ->  authorities.add(new SimpleGrantedAuthority(privilege.getName())));
        ((CustomPrincipal) auth.getPrincipal()).setUser(user);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(),authorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
