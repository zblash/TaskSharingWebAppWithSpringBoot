package com.tasksharing.tasksharing.Security;

import com.tasksharing.tasksharing.models.CustomPrincipal;
import com.tasksharing.tasksharing.models.Group;
import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.services.Concrete.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpression extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private UserService userService;

    Logger logger = LoggerFactory.getLogger(CustomMethodSecurityExpression.class);

    public CustomMethodSecurityExpression(Authentication authentication) {
        super(authentication);
    }

    public boolean isMember(String slugName) {
        User user = ((CustomPrincipal) this.authentication.getPrincipal()).getUser();

        return hasGroup(user,slugName);
    }

    private boolean hasGroup(User user, String groupSlugName){
        for (Group group : user.getGroups()){
            logger.info(group.getSlugName());
            logger.info(groupSlugName);
            if(groupSlugName.equals(group.getSlugName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void setFilterObject(Object o) {

    }

    @Override
    public Object getFilterObject() {
        return null;
    }

    @Override
    public void setReturnObject(Object o) {

    }

    @Override
    public Object getReturnObject() {
        return null;
    }

    @Override
    public Object getThis() {
        return null;
    }
}
