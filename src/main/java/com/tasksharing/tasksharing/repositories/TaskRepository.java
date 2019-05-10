package com.tasksharing.tasksharing.repositories;

import com.tasksharing.tasksharing.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findAllByGroup_Id(Long groupId);
}
