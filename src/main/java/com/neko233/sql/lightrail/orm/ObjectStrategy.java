package com.neko233.sql.lightrail.orm;


import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author SolarisNeko
 * @date 4/23/2022
 */
@Slf4j
public class ObjectStrategy implements ConvertStrategy {

    @Override
    public Object singleColumn(ResultSet thisRowRs) throws IllegalAccessException, InstantiationException, SQLException {
        return null;
    }

    @Override
    public Object columnNameConvert(ResultSet thisRowRs, String columnName) throws SQLException, IllegalAccessException, InstantiationException {
        return null;
    }

    @Override
    public Object objectConvert(ResultSet thisRowRs, String columnName,
                                Class<?> returnType, Map<String, Field> haveAnnoFields) throws SQLException, IllegalAccessException, InstantiationException {
        Object newTargetObj = returnType.newInstance();
        for (Map.Entry<String, Field> columnName2Field : haveAnnoFields.entrySet()) {
            setFieldByType(
                    thisRowRs,
                    columnName2Field.getKey(),
                    columnName2Field.getValue(),
                    newTargetObj
            );
        }
        return newTargetObj;
    }


    /**
     * ORM set value to Instance
     *
     * @param rs         结果集
     * @param columnName 列名 createTime
     * @param field      字段名, create_time
     * @param instance   实例对象
     * @param <T>        范型 for 实例
     * @throws IllegalAccessException 反射非法获取
     */
    private static <T> void setFieldByType(ResultSet rs, String columnName, Field field, T instance)
            throws IllegalAccessException {
        field.setAccessible(true);

        ConvertStrategy convertStrategy = ConvertStrategyFactory.choose(field.getType());

        Object rsValue = null;
        try {
            rsValue = convertStrategy.autoMatch(rs, columnName, null, null);
        } catch (SQLException e) {
            log.error("get column from DB error. column name = {}", columnName, e);
        } catch (InstantiationException e) {
            log.error("recursive instantiate error. instance type = {}", instance.getClass(), e);
        }

        boolean notSupportType = ConvertStrategyFactory.isNotSupportBaseType(field.getType());
        if (notSupportType) {
            throw new IllegalArgumentException("not support base type. please change to Wrapper type. Like bool -> Boolean. field = " + field.getName());
        }

        // set value
        field.set(instance, rsValue);
    }


}
