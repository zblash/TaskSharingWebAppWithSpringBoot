package com.tasksharing.tasksharing.models;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Set;

public class UserTaskModel {

    @NotEmpty
    private Set<User> users;

    @NotEmpty
    private Set<Task> tasks;

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
