package com.tasksharing.tasksharing.models;

import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3,max = 20)
    private String name;

    @NotNull
    @Size(min = 3,max = 150)
    private String description;

    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_task",
            joinColumns =
            @JoinColumn(name = "task_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> users;

    public Task() {
    }

    public Task(String name, String description, Group group) {
        this.name = name;
        this.description = description;
        this.group = group;
    }

    public void addUser(User user){
        users.add(user);
    }

    public void removeUser(User user){
        users.remove(user);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
