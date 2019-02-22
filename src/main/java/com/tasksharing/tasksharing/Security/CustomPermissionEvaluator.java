package com.tasksharing.tasksharing.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    Logger logger = LoggerFactory.getLogger(CustomPermissionEvaluator.class);

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
            if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)){
                return false;
            }
            String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();
            return hasPrivilege(authentication, targetType, permission.toString().toUpperCase());
    }


    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String targetType, Object permission) {
        if ((authentication == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }

        return hasPrivilege(authentication, targetType.toUpperCase(),
                permission.toString().toUpperCase());

    }


    private boolean hasPrivilege(Authentication authentication, String targetType, String permission) {
        for (GrantedAuthority grantedAuth : authentication.getAuthorities()) {
            if (grantedAuth.getAuthority().startsWith(targetType)) {
                if (grantedAuth.getAuthority().contains(permission)) {
                    return true;
                }
            }
        }
        return false;
    }
}
