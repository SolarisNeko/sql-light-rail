package com.neko233.lightrail.orm;

import com.neko233.lightrail.util.CamelCaseUtil;
import com.neko233.lightrail.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * @author SolarisNeko
 * Date on 2022-02-26
 */
@Slf4j
public class RailPlatformOrm {

    /**
     * ORM convert
     *
     * @param rs         SQL 结果集
     * @param returnType 要生成的 object 的 Class
     * @param <T>        范型
     * @return SQL ResultSet 通过 ORM 映射后的 Java DataList
     */
    public static <T> List<T> orm(ResultSet rs, Class<?> returnType) {
        List<Field> fieldList = ReflectUtil.getAllFields(returnType);
        Map<String, String> fieldColumnMap = fieldList.stream()
            .collect(toMap(
                Field::getName,
                field -> CamelCaseUtil.getBigCamelLowerName(field.getName()),
                (t1, t2) -> t1)
            );

        List<T> dataList = new ArrayList<>();
        try {
            while (rs.next()) {
                T newObject = (T) returnType.newInstance();
                for (Field field : fieldList) {
                    setFieldByType(rs, fieldColumnMap.get(field.getName()), field, newObject);
                }
                dataList.add(newObject);
            }
        } catch (Exception e) {
            log.error("[RailPlatformOrm] ORM Mapping error!", e);
            return dataList;
        }

        return dataList;
    }

    public RailPlatformOrm() {
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
        String typeName = field.getType().getSimpleName();
        switch (typeName) {
            case "int":
            case "Integer":
            case "java.lang.Integer": {
                Integer rsValue;
                try {
                    rsValue = rs.getInt(columnName);
                } catch (SQLException e) {
                    rsValue = null;
                }
                field.set(instance, rsValue);
                break;
            }
            case "float":
            case "Float":
            case "java.lang.Float": {
                Float rsValue;
                try {
                    rsValue = rs.getFloat(columnName);
                } catch (SQLException e) {
                    rsValue = null;
                }
                field.set(instance, rsValue);
                break;
            }
            case "double":
            case "Double":
            case "java.lang.Double": {
                Double rsValue;
                try {
                    rsValue = rs.getDouble(columnName);
                } catch (SQLException e) {
                    rsValue = null;
                }
                field.set(instance, rsValue);
                break;
            }
            case "long":
            case "Long":
            case "java.lang.Long": {
                Long rsValue;
                try {
                    rsValue = rs.getLong(columnName);
                } catch (SQLException e) {
                    rsValue = null;
                }
                field.set(instance, rsValue);
                break;
            }
            case "String":
            case "java.lang.String": {
                String rsValue;
                try {
                    rsValue = rs.getString(columnName);
                } catch (SQLException e) {
                    rsValue = null;
                }
                field.set(instance, rsValue);
                break;
            }
            case "BigDecimal":
            case "java.math.BigDecimal": {
                BigDecimal value;
                try {
                    value = new BigDecimal(rs.getInt(columnName));
                } catch (SQLException e) {
                    value = null;
                }
                field.set(instance, value);
                break;
            }
            case "boolean":
            case "Boolean":
            case "java.lang.Boolean": {
                Boolean rsValue;
                try {
                    rsValue = rs.getBoolean(columnName);
                } catch (SQLException e) {
                    rsValue = null;
                }
                field.set(instance, rsValue);
                break;
            }
            case "Date":
            case "java.util.Date": {
                // datetime = "yyyy-MM-dd hh:mm:ss"
                Date rsValue;
                try {
                    rsValue = rs.getDate(columnName);
                } catch (SQLException e) {
                    rsValue = null;
                }
                field.set(instance, rsValue);
                break;
            }
            case "byte":
            case "Byte":
            case "java.lang.Byte": {
                byte[] rsValue;
                try {
                    rsValue = rs.getBytes(columnName);
                } catch (SQLException e) {
                    rsValue = null;
                }
                field.set(instance, rsValue);
                break;
            }
            default: {
                throw new RuntimeException("[RailPlatformOrm] ORM 不支持该类型");
            }
        }
    }


}
