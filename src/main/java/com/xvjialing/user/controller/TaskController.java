package com.xvjialing.user.controller;

import com.xvjialing.user.domain.Task;
import com.xvjialing.user.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/tasks")
    public List<Task> getTaskList(){
        return taskRepository.findAll();
    }

    @PostMapping("/tasks")
    public Task addTask(@RequestParam(value = "hour",required = true) int hour,
                        @RequestParam("minute") int minute,
                        @RequestParam("second") int second,
                        @RequestParam("isRepeat") boolean isRepeat,
                        @RequestParam("tag") String tag){
        Task task=new Task();
        task.setRepeat(isRepeat);
        task.setSecond(second);
        task.setHour(hour);
        task.setMinute(minute);
        task.setTag(tag);

        return taskRepository.save(task);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable("id") int id){
        taskRepository.delete(id);
    }


}
