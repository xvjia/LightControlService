package com.xvjialing.user.controller;

import com.xvjialing.user.domain.LoginReturn;
import com.xvjialing.user.domain.User;
import com.xvjialing.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public boolean login(@RequestParam("userName") String userName,
                         @RequestParam("password") String password){
        return userRepository.existsByUserNameAndAndPassword(userName,password);
    }

    public boolean checkUserExists(String userName){
        return userRepository.existsByUserName(userName);
    }

    @PostMapping("/register")
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
            loginReturn.setUser(user);
            return loginReturn;
        }


    }
}
