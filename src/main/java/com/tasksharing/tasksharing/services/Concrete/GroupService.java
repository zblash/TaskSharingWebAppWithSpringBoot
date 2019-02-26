package com.tasksharing.tasksharing.services.Concrete;

import com.tasksharing.tasksharing.models.Group;
import com.tasksharing.tasksharing.models.Privilege;
import com.tasksharing.tasksharing.models.User;
import com.tasksharing.tasksharing.repositories.GroupRepository;
import com.tasksharing.tasksharing.repositories.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    public List<Group> findAll() {

        return groupRepository.findAll();
    }

    public Group findById(Long taskId){
        Optional<Group> group = groupRepository.findById(taskId);
        return group.orElseThrow(RuntimeException::new);
    }

    public void Add(Group group){
        groupRepository.save(group);
    }


    public Group Update(Group group){
        return groupRepository.saveAndFlush(group);
    }

    public Group findBySlugName(String name) {
        return groupRepository.findBySlugName(name);
    }

    public void removeGroup(String slugname){
        Group group = groupRepository.findBySlugName(slugname);

        for (User user : group.getUsers()) {
            user.removeGroup(group);
            for(Privilege privilege : user.getPrivileges()){
                if (privilege.getName().startsWith(slugname.toUpperCase())){
                    user.removePrivilege(privilege);
                    privilegeRepository.delete(privilege);
                }
            }
        }
        groupRepository.delete(group);
    }
}
