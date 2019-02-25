package com.tasksharing.tasksharing.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

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
    private Collection<Task> tasks = new ArrayList<>();

    @ManyToMany(mappedBy = "groups",fetch = FetchType.EAGER)
    private Collection<User> users = new ArrayList<>();

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

    public Group(String groupName, Long user_id, String description, Collection<Task> tasks, Collection<User> users) {
        this.groupName = groupName;
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

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }


}
