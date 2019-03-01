package com.tasksharing.tasksharing.services.Concrete;

import com.tasksharing.tasksharing.models.Group;
import com.tasksharing.tasksharing.models.Privilege;
import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.repositories.GroupRepository;
import com.tasksharing.tasksharing.repositories.PrivilegeRepository;
import com.tasksharing.tasksharing.services.Abstract.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService implements IGroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Override
    public List<Group> findAll() {

        return groupRepository.findAll();
    }

    @Override
    public Group findById(Long taskId){
        Optional<Group> group = groupRepository.findById(taskId);
        return group.orElseThrow(RuntimeException::new);
    }

    @Override
    public void Add(Group group){
        groupRepository.save(group);
    }

    @Override
    public Group Update(Group group){
        return groupRepository.saveAndFlush(group);
    }

    @Override
    public Group Update(Group updateGroup,Group changeto){
        updateGroup.getUsers().forEach(user -> {
            user.getPrivileges().stream().
                    filter(privilege -> privilege.getName().startsWith(updateGroup.getSlugName().toUpperCase()))
                    .forEach(privilege -> {
                        String[] privilege_types = privilege.getName().split("_");
                        privilege.setName(changeto.getSlugName().toUpperCase()+"_"+privilege_types[1]);
                        privilegeRepository.save(privilege);
                    });
        });
        updateGroup.setGroupName(changeto.getGroupName());
        updateGroup.setSlugName(changeto.getSlugName());
        updateGroup.setDescription(changeto.getDescription());
        return groupRepository.save(updateGroup);


    }

    @Override
    public Group findBySlugName(String name) {
        return groupRepository.findBySlugName(name);
    }

    @Override
    public void removeGroup(String slugname){
        Group group = groupRepository.findBySlugName(slugname);
        group.getUsers().forEach(user -> {
            user.removeGroup(group);
            privilegeRepository.findAll().stream().
                    filter(privilege -> privilege.getName().startsWith(slugname.toUpperCase()))
                    .forEach(privilege -> {
                user.removePrivilege(privilege);
                privilegeRepository.delete(privilege);
            });
        });
        groupRepository.delete(group);
    }
}
