package com.tasksharing.tasksharing.Security.JWTAuthentication;

import com.tasksharing.tasksharing.Security.JWTAuthToken.JWTAuthToken;
import com.tasksharing.tasksharing.Security.JWTAuthToken.JWTValidator;
import com.tasksharing.tasksharing.models.CustomPrincipal;
import com.tasksharing.tasksharing.models.Privilege;
import com.tasksharing.tasksharing.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class JWTAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private JWTValidator validator;

    private final Logger logger = LoggerFactory.getLogger(JWTAuthenticationProvider.class);
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        JWTAuthToken jwtAuthenticationToken = (JWTAuthToken) authentication;
        String token = jwtAuthenticationToken.getToken();

        User user = validator.validate(token);
        if (user == null) {
            throw new AuthenticationCredentialsNotFoundException("JWT Token is incorrect");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Privilege privilege : user.getPrivileges()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(privilege.getName()));
        }
        return new CustomPrincipal(user,user.getUserName(), user.getId(), token, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JWTAuthToken.class.isAssignableFrom(aClass));
    }

}