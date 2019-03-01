package com.tasksharing.tasksharing.services.Abstract;

import com.tasksharing.tasksharing.models.Group;

import java.util.List;
import java.util.Optional;

public interface IGroupService {

    List<Group> findAll();

    Group findById(Long taskId);

    void Add(Group group);

    Group Update(Group group);

    Group Update(Group updateGroup,Group changeto);

    Group findBySlugName(String name);

    void removeGroup(String slugname);
}
