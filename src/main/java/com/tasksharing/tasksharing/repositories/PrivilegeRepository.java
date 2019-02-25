package com.tasksharing.tasksharing.repositories;

import com.tasksharing.tasksharing.models.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

@Transactional
public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {

    Privilege findByName(String name);

}
