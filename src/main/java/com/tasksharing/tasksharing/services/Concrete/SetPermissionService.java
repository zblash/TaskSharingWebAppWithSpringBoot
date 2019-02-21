package com.tasksharing.tasksharing.services.Concrete;

import com.tasksharing.tasksharing.models.Privilege;
import com.tasksharing.tasksharing.models.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

public class SetPermissionService {

    private Authentication authentication;

    public SetPermissionService(Authentication authentication) {
        this.authentication = authentication;
    }

    public void Set(User user){
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Privilege privilege : user.getPrivileges()) {
            authorities.add(new SimpleGrantedAuthority(privilege.getName()));
        }
        Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(),authorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
