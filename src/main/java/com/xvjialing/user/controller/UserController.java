package com.xvjialing.user.controller;

import com.xvjialing.user.domain.LoginReturn;
import com.xvjialing.user.domain.User;
import com.xvjialing.user.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Api(value = "用户操作",description = "用户操作相关API")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/login",produces = "application/json")
    @ApiOperation(value="登陆", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string",paramType = "query"),
    })
    public boolean login(@RequestParam("userName") String userName,
                         @RequestParam("password") String password){
        return userRepository.existsByUserNameAndAndPassword(userName,password);
    }

    public boolean checkUserExists(String userName){
        return userRepository.existsByUserName(userName);
    }

    @PostMapping(value = "/register",produces = "application/json")
    @ApiOperation(value="注册", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "string",paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string",paramType = "query"),
    })
    public LoginReturn addUser(@RequestParam("userName") String userName,
                        @RequestParam("password") String password){
        boolean userNameExists=checkUserExists(userName);
        LoginReturn loginReturn=new LoginReturn();
        if (userNameExists){

            loginReturn.setStatus(false);
            loginReturn.setMsg("用户名已存在");
            return loginReturn;
        }else {
            User user=new User();
            user.setUserName(userName);
            user.setPassword(password);
            loginReturn.setStatus(true);
            loginReturn.setMsg("注册成功");
            loginReturn.setUser(userRepository.save(user));
            return loginReturn;
        }
        
    }
}
