package com.example.study.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Component;

import com.example.study.dao.dataobject.UserDO;
import com.example.study.dao.mapper.UserMapper;
import com.example.study.api.UserService;
import com.example.study.api.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private static final BeanCopier copier = BeanCopier.create(UserModel.class, UserDO.class, false);

    public String getUserName(Long id) {
        UserDO userDO = userMapper.getById(id);
        return userDO != null ? userDO.getName() : null;
    }

    public UserModel addUser(UserModel user) {
        UserDO userDO = new UserDO();
        copier.copy(user, userDO, null);

        Long id = userMapper.insert(userDO);
        user.setId(id);
        return user;
    }

    @Override
    public boolean addUsers(List<UserModel> userList) {
        List<UserDO> userDOList = new ArrayList<>();
        for (UserModel user : userList) {
            UserDO userDO = new UserDO();
            copier.copy(user, userDO, null);
            userDOList.add(userDO);
        }
        userMapper.insertBatch(userDOList);
        return true;
    }

    @Override
    public UserModel addUserByMap(UserModel user) {
        HashMap param = new HashMap();
        param.put("name", user.getName());
        param.put("age", user.getAge());

        Long id = userMapper.insertByMap(param);
        user.setId(id);
        return user;
    }

    @Override
    public UserModel addUserByParams(UserModel user) {
        Long id = userMapper.insertByParams(user.getName(), user.getAge());
        user.setId(id);
        return user;
    }
}
