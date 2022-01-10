package com.example.study.dao.mapper;

import com.example.study.dao.dataobject.UserDO;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Mapper
public interface UserMapper {

    UserDO getByName(String name);

    UserDO getById(Long id);

    Long insert(UserDO userDO);

    Long insertBatch(List<UserDO> list);

    Long insertByMap(HashMap param);

    /**
     * [arg1, arg0, param1, param2]
     * @param name
     * @param age
     * @return
     */
    Long insertByParams(@Param("name") String name, @Param("age") Integer age);
}
