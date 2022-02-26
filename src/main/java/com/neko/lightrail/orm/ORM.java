package com.neko.lightrail.orm;

import com.neko.lightrail.util.CamelCaseUtil;
import com.neko.lightrail.util.ReflectUtil;

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
 * @date 2022-02-26
 */
public class ORM {


    /**
     * ORM convert
     * @param rs SQL 结果集
     * @param clazz 要生成的 object 的 Class
     * @param <T> 范型
     * @return SQL ResultSet 通过 ORM 映射后的 Java DataList
     */
    public static <T> List<T> convert(ResultSet rs, Class clazz) {
        List<Field> fieldList = ReflectUtil.getAllFields(clazz);
        Map<String, String> fieldColumnMap = fieldList.stream()
            .collect(toMap(
                Field::getName,
                field -> CamelCaseUtil.getBigCamelLowerName(field.getName()),
                (t1, t2) -> t1)
            );

        List<T> dataList = new ArrayList<>();
        try {
            while (rs.next()) {
                T newObject = (T) clazz.newInstance();
                for (Field field : fieldList) {
                    setFieldByType(rs, fieldColumnMap.get(field.getName()), field, newObject);
                }
                dataList.add(newObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private static <T> void setFieldByType(ResultSet rs, String columnName, Field field, T newObject)
        throws SQLException, IllegalAccessException {
        field.setAccessible(true);
        String typeName = field.getType().getSimpleName();
        switch (typeName) {
            case "Integer":
            case "java.lang.Integer": {
                field.set(newObject, rs.getInt(columnName));
                break;
            }
            case "Float":
            case "java.lang.Float": {
                field.set(newObject, rs.getFloat(columnName));
                break;
            }
            case "Double":
            case "java.lang.Double": {
                field.set(newObject, rs.getDouble(columnName));
                break;
            }
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
