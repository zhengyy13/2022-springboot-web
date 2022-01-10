/*
 * Copyright (c) 2001-2022 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.example.study.dao.plugin;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

/**
 * TODO
 *
 * @author zhengyy1
 * @version V1.0
 * @since 2022-01-07 16:11
 */
@Intercepts(@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }))
public class InsertPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        autoFill(invocation);
        return invocation.proceed();
    }

    private void autoFill(Invocation invocation) {
        Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        Object parameter = args[1];
        SqlCommandType sqlCommandType = statement.getSqlCommandType();
        if (sqlCommandType.equals(SqlCommandType.INSERT)) {
            //判断参数是实体还是集合
            if (parameter instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameter;
                for (Object value : paramMap.values()) {
                    if (Objects.nonNull(value)) {
                        if (List.class.isAssignableFrom(value.getClass())) {
                            List list = (List) value;
                            for (Object o : list) {
                                autoFillUser(o);
                            }
                        }
                    }
                }
            }
            autoFillUser(parameter);
        }
    }

    private void autoFillUser(Object object) {
        MetaObject metaObject = SystemMetaObject.forObject(object);
        try {
            String name = (String) metaObject.getValue("name");
            if (StringUtils.isBlank(name)) {
                metaObject.setValue("name", UUID.randomUUID().toString().substring(0, 8));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            Integer age = (Integer) metaObject.getValue("age");

            if (Objects.isNull(age)) {
                metaObject.setValue("age", new Random().nextInt(100));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
