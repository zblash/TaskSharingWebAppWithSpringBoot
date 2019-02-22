package com.tasksharing.tasksharing.repositories;

import com.tasksharing.tasksharing.models.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {

    Privilege findByName(String name);

}
