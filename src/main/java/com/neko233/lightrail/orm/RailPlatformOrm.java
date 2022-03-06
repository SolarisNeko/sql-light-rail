package com.neko233.lightrail.orm;

import com.neko233.lightrail.util.CamelCaseUtil;
import com.neko233.lightrail.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
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
     * @param rs SQL 结果集
     * @param returnType 要生成的 object 的 Class
     * @param <T> 范型
     * @return SQL ResultSet 通过 ORM 映射后的 Java DataList
     */
    public static <T> List<T> mapping(ResultSet rs, Class<?> returnType) {
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
            log.error("ORM Mapping error! Exception = {}", e.getMessage());
            return dataList;
        }

        return dataList;
    }

    private static <T> void setFieldByType(ResultSet rs, String columnName, Field field, T newObject)
        throws SQLException, IllegalAccessException {
        field.setAccessible(true);
        String typeName = field.getType().getSimpleName();
        switch (typeName) {
            case "int":
            case "Integer":
            case "java.lang.Integer": {
                field.set(newObject, rs.getInt(columnName));
                break;
            }
            case "float":
            case "Float":
            case "java.lang.Float": {
                field.set(newObject, rs.getFloat(columnName));
                break;
            }
            case "double":
            case "Double":
            case "java.lang.Double": {
                field.set(newObject, rs.getDouble(columnName));
                break;
            }
            case "long":
            case "Long":
            case "java.lang.Long": {
                field.set(newObject, rs.getLong(columnName));
                break;
            }
            case "String":
            case "java.lang.String": {
                field.set(newObject, rs.getString(columnName));
                break;
            }
            case "BigDecimal":
            case "java.math.BigDecimal": {
                field.set(newObject, new BigDecimal(rs.getInt(columnName)));
                break;
            }
            case "boolean":
            case "Boolean":
            case "java.lang.Boolean": {
                field.set(newObject, rs.getBoolean(columnName));
                break;
            }
            case "Date":
            case "java.util.Date": {
                // datetime = "yyyy-MM-dd hh:mm:ss"
                field.set(newObject, rs.getDate(columnName));
                break;
            }
            case "byte":
            case "Byte":
            case "java.lang.Byte": {
                field.set(newObject, rs.getBytes(columnName));
                break;
            }
            default: {
                throw new RuntimeException("[ORM] 不支持该类型");
            }
        }
    }


}
