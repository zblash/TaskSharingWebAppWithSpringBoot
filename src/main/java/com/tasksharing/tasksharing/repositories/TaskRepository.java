package com.tasksharing.tasksharing.repositories;

import com.tasksharing.tasksharing.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
