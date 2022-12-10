package com.neko233.sql.lightrail.strategy.type;

import com.neko233.sql.lightrail.strategy.TypeStrategy;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author SolarisNeko
 * @date 4/23/2022
 */
public class ObjectStrategy implements TypeStrategy {

    @Override
    public Object getOrmValue(ResultSet nextRs, Class<?> returnType, List<Field> returnTypeFieldList, Map<String, String> field2ColumnNameMap) throws IllegalAccessException, InstantiationException {
        Object newObject = returnType.newInstance();
        for (Field field : returnTypeFieldList) {
            setFieldByType(nextRs, field2ColumnNameMap.get(field.getName()), field, newObject);
        }
        return newObject;
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
            case "short":
            case "Short":
            case "java.lang.Short": {
                Short rsValue;
                try {
                    rsValue = rs.getShort(columnName);
                } catch (SQLException e) {
                    rsValue = null;
                }
                field.set(instance, rsValue);
                break;
            }
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
            case "java.math.BigInteger": {
                BigInteger rsValue;
                try {
                    rsValue = BigInteger.valueOf(rs.getLong(columnName));
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
