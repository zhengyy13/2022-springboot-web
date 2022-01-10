package com.example.study.dao.plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

@Intercepts(@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }))
public class AutoFillTentantIdPlugin implements Interceptor {

    private Log log = LogFactory.getLog(getClass());

    @Override
    public Object intercept(Invocation invocation) throws IllegalAccessException, InvocationTargetException {
        //        String origSql = getBound(invocation);
        //        log.debug("原始sql：" + origSql);
        fillTentantId(invocation);
        //        String finalSql = getBound(invocation);
        //        log.debug("最终sql：" + finalSql);
        return invocation.proceed();
    }

    private void fillTentantId(Invocation invocation) throws IllegalAccessException {
        Object[] args = invocation.getArgs();
        SqlCommandType sqlCommandType = null;
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Class<?> aClass = arg.getClass();
            String className = aClass.getName();
            log.debug(i + " 参数类型：" + className);
            //第一个参数处理。根据它判断是否给“操作属性”赋值。
            if (arg instanceof MappedStatement) {//如果是第一个参数 MappedStatement
                MappedStatement ms = (MappedStatement) arg;
                sqlCommandType = ms.getSqlCommandType();
                log.debug("操作类型：" + sqlCommandType);
                if (sqlCommandType == SqlCommandType.INSERT) {
                    continue;
                } else {
                    break;
                }
            }

            if (sqlCommandType == SqlCommandType.INSERT) {
                /**
                 * 参数：
                 * org.apache.ibatis.binding.MapperMethod$ParamMap
                 * list
                 * 正常对象
                 */
                for (Field f : aClass.getDeclaredFields()) {
                    f.setAccessible(true);
                    switch (f.getName()) {
                    case "currentTenantId":
                        /**
                         * 用户信息
                         * 1.rest接口ak，uk
                         * 2.dubbo接口
                         * 3.无用户信息，如mq
                         */
                        //提前设置了值优先
                        String currentTenantId = getProperty(arg, "currentTenantId");
                        if (StringUtils.isBlank(currentTenantId)) {
                            setProperty(arg, "currentTenantId", 0L);
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * 为对象的操作属性赋值
     *
     * @param bean
     */
    private void setProperty(Object bean, String name, Object value) {
        try {
            //根据需要，将相关属性赋上默认值
            BeanUtils.setProperty(bean, name, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private String getProperty(Object bean, String name) {
        String value = null;
        try {
            //根据需要，获取相关属性值
            value = BeanUtils.getProperty(bean, name);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private String getBound(Invocation invocation) {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        return boundSql.getSql();
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
            ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
