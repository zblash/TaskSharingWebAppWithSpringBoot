package com.tasksharing.tasksharing.services.Concrete;

import com.tasksharing.tasksharing.models.CustomPrincipal;
import com.tasksharing.tasksharing.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    public UserDetailsServiceImp(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
       try{
        User user = userService.findByUserName(s);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomPrincipal(user);

    }catch (Exception e){
        throw new UsernameNotFoundException("User not found");
    }
    }

}
