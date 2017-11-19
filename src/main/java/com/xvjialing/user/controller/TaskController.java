package com.xvjialing.user.controller;

import com.xvjialing.user.domain.Task;
import com.xvjialing.user.repository.TaskRepository;
import io.swagger.annotations.*;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(value = "定时任务",description = "定时任务相关API")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping(value = "/tasks",produces = "application/json")
    @ApiOperation(value="获取定时任务列表", notes="")
    public List<Task> getTaskList(){
        return taskRepository.findAll();
    }

    @PostMapping(value = "/tasks",produces = "application/json")
    @ApiOperation(value="添加定时任务", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hour", value = "时", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "minute", value = "分", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "isRepeat", value = "是否重复", required = true, dataType = "boolean",paramType = "query"),
            @ApiImplicitParam(name = "tag", value = "信号", required = true, dataType = "string",paramType = "query")
    })
    public Task addTask(@RequestParam(value = "hour",required = true) int hour,
                        @RequestParam("minute") int minute,
                        @RequestParam("isRepeat") boolean isRepeat,
                        @RequestParam("tag") String tag){
        Task task=new Task();
        task.setRepeat(isRepeat);
        task.setHour(hour);
        task.setMinute(minute);
        task.setTag(tag);

        Task task1 = taskRepository.save(task);
        getTasks();

        return task1;
    }

    @PutMapping(value = "/tasks/{id}",produces = "application/json")
    @ApiOperation(value="更新定时任务", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int",paramType = "path"),
            @ApiImplicitParam(name = "id", value = "taskId", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "hour", value = "时", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "minute", value = "分", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "isRepeat", value = "是否重复", required = true, dataType = "boolean",paramType = "query"),
            @ApiImplicitParam(name = "tag", value = "信号", required = true, dataType = "string",paramType = "query")
    })
    public Task updateTask(@PathVariable("id") int id,
                           @RequestParam("id") int taskId,
                           @RequestParam(value = "hour",required = true) int hour,
                           @RequestParam("minute") int minute,
                           @RequestParam("isRepeat") boolean isRepeat,
                           @RequestParam("tag") String tag){
        Task task=new Task();
        task.setId(taskId);
        task.setRepeat(isRepeat);
        task.setHour(hour);
        task.setMinute(minute);
        task.setTag(tag);
        Task save = taskRepository.save(task);

        getTasks();

        return save;
    }

    @ApiOperation(value = "删除定时任务")
    @ApiImplicitParam(name = "id", value = "定时任务ID", required = true, dataType = "int" ,paramType = "path")
    @DeleteMapping(value = "/tasks/{id}",produces = "application/json")
    public void deleteTask(@PathVariable("id") int id){
        taskRepository.delete(id);
        getTasks();
    }

    public void getTasks(){

        String topic        = "lightControl";
        String content      = "getTasks";
        int qos             = 2;
        String broker       = "tcp://45.77.159.109:32768";
        String clientId     = "lightcontrol"+ Calendar.getInstance().getTimeInMillis();
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient mqttClient=new MqttClient(broker,clientId,persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            mqttClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            mqttClient.publish(topic, message);
            System.out.println("Message published");
            mqttClient.disconnect();
            System.out.println("Disconnected");
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

}
