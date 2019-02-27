package com.tasksharing.tasksharing.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String groupName;

    @NotNull
    private String slugName;

    @Transient
    private Long user_id;

    @NotNull
    private String description;

    @OneToMany(mappedBy = "group",cascade = CascadeType.REMOVE,orphanRemoval = true)
    private Set<Task> tasks;

    @ManyToMany(mappedBy = "groups",fetch = FetchType.EAGER)
    private Set<User> users;

    public void addUser(User user){
        users.add(user);
        user.addGroup(this);
    }

    public void removeUser(User user){
        users.remove(user);
        user.removeGroup(this);
    }

    public Group() {

    }

    public Group(@NotNull String groupName, @NotNull String slugName, Long user_id, @NotNull String description, Set<Task> tasks, Set<User> users) {
        this.groupName = groupName;
        this.slugName = slugName;
        this.user_id = user_id;
        this.description = description;
        this.tasks = tasks;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSlugName() {
        return slugName;
    }

    public void setSlugName(String slugName) {
        this.slugName = slugName;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }


}
