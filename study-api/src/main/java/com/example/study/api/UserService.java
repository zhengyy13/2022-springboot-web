package com.example.study.api;


import com.example.study.api.model.UserModel;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
public interface UserService {

    String getUserName(Long id);

    UserModel addUser(UserModel user);

    boolean addUsers(List<UserModel> userList);

    UserModel addUserByMap(UserModel user);

    UserModel addUserByParams(UserModel user);
}
