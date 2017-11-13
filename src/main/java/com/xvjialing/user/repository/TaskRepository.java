package com.xvjialing.user.repository;

import com.xvjialing.user.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Integer>{

}
