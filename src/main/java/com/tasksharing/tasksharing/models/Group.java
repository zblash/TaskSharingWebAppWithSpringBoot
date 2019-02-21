package com.tasksharing.tasksharing.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String groupName;

    @Transient
    private Long user_id;

    private String description;

    @OneToMany(mappedBy = "group",fetch = FetchType.LAZY)
    private Collection<Task> tasks = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Collection<User> users = new ArrayList<>();

    public void addUser(User user){
        users.add(user);
        user.getGroups().add(this);
    }

    public void removeUser(User user){
        users.remove(user);
        user.getGroups().remove(this);

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
