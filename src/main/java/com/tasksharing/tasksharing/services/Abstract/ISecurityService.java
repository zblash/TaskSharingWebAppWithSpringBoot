package com.tasksharing.tasksharing.services.Abstract;

import com.tasksharing.tasksharing.models.User;

public interface ISecurityService {

    String findLoggedInUsername();

    User findLoggedInUser();

    void reloadLoggedInUser();

}
