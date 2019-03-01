package com.tasksharing.tasksharing.Security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

public class CustomPermissionEvaluator implements PermissionEvaluator {

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
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuth -> grantedAuth.getAuthority().startsWith(targetType)
                        && grantedAuth.getAuthority().contains(permission));

    }
}
