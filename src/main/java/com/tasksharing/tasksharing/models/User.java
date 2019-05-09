package com.tasksharing.tasksharing.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3,max = 25)
    private String userName;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min = 3,max = 25)
    private String firstName;

    @NotNull
    @Size(min = 3,max = 25)
    private String lastName;

    @NotNull
    @Size(min = 5,max = 90)
    private String password;

    private String resetToken;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_group",
            joinColumns =
            @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "group_id", referencedColumnName = "id"))
    private Set<Group> groups;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "user_privilege",
            joinColumns =
            @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges;

    public void addGroup(Group group){
        groups.add(group);
    }

    public void removeGroup(Group group){
        groups.remove(group);
    }

    public void addPrivilege(Privilege privilege){
        privileges.add(privilege);
    }

    public void removePrivilege(Privilege privilege){
        privileges.remove(privilege);
    }

    public User() {
    }

    public User(@NotNull @Size(min = 3, max = 25) String userName, @Email @NotNull String email, @NotNull @Size(min = 3, max = 25) String firstName, @NotNull @Size(min = 3, max = 25) String lastName, @NotNull @Size(min = 5, max = 90) String password, Set<Group> groups, Set<Privilege> privileges) {
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.groups = groups;
        this.privileges = privileges;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

}
