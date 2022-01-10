package com.example.study.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.study.api.UserService;
import com.example.study.api.model.UserModel;

import java.util.List;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Component
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/username")
    public String getUserName(@RequestParam("id") Long id) {
        return userService.getUserName(id);
    }

    @RequestMapping("/add")
    @ResponseBody
    public UserModel addUser(@RequestParam("name") String name, @RequestParam("age") Integer age) {
        UserModel user = new UserModel();
        user.setName(name);
        user.setAge(age);
        return userService.addUser(user);
    }

    @RequestMapping("/addList")
    @ResponseBody
    public Boolean addUserList(@RequestBody List<UserModel> userModelList) {
        return userService.addUsers(userModelList);
    }

    @RequestMapping("/addByMap")
    @ResponseBody
    public UserModel addUserByMap(@RequestParam("name") String name, @RequestParam("age") Integer age) {
        UserModel user = new UserModel();
        user.setName(name);
        user.setAge(age);
        return userService.addUserByMap(user);
    }

    @RequestMapping("/addByParams")
    @ResponseBody
    public UserModel addUserByParams(@RequestParam("name") String name, @RequestParam("age") Integer age) {
        UserModel user = new UserModel();
        user.setName(name);
        user.setAge(age);
        return userService.addUserByParams(user);
    }
}
