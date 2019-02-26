package com.tasksharing.tasksharing.models;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;

public class UserTaskModel {

    @NotEmpty
    private Collection<User> users;

    @NotEmpty
    private Collection<Task> tasks;

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }
}
