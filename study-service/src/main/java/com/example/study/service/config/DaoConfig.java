/*
 * Copyright (c) 2001-2022 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.example.study.service.config;

import com.example.study.dao.plugin.AutoFillTentantIdPlugin;
import com.example.study.dao.plugin.InsertPlugin;
import com.example.study.dao.plugin.QueryPlugin;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author zhengyy1
 * @version V1.0
 * @since 2022-01-05 10:08
 */
@Configuration
public class DaoConfig {

//    @Bean
//    public QueryPlugin queryPlugin() {
//        QueryPlugin queryPlugin = new QueryPlugin();
//        Properties properties = new Properties();
//        properties.setProperty("name", "queryPlugin");
//        queryPlugin.setProperties(properties);
//        return queryPlugin;
//    }

//    @Bean
//    public AutoFillTentantIdPlugin autoFillTentantIdPlugin() {
//        return new AutoFillTentantIdPlugin();
//    }

    @Bean
    public InsertPlugin insertPlugin() {
        return new InsertPlugin();
    }
}
