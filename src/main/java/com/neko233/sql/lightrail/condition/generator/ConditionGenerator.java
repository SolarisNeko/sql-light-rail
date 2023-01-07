package com.neko233.sql.lightrail.condition.generator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SQL 条件生成器
 *
 * @author LuoHaoJun on 2022-08-01
 **/
@Slf4j
public class ConditionGenerator {

    public static String condition(String columnName, SqlOperation operation, Object value) {
        return condition(columnName, operation, value, true);
    }

    public static String condition(String columnName, SqlOperation operation, Object value, boolean addAnd) {
        String operateWithSpace = chooseSqlOperate(operation);

        String tempValue;
        if (value instanceof Collection) {
            if (Objects.equals(operation, SqlOperation.IN) || Objects.equals(operation, SqlOperation.NOT_IN) ) {
                // ok
            } else {
                throw new RuntimeException(String.format("When your value type is Collection, SqlOperation must is 'in', 'not in'. your operation = %s", operateWithSpace));
            }

            Collection collection = (Collection) value;
            boolean empty = CollectionUtils.isEmpty(collection);
            if (empty) {
                return "";
            }

            List<String> itemConvertList = (List<String>) collection.stream()
                    .map(item -> {
                        if (item instanceof String) {
                            String afterItem = ((String) item).replaceAll("'", "''");
                            return "'" + afterItem + "'";
                        } else {
                            return String.valueOf(item);
                        }
                    })
                    .collect(Collectors.toList());
            tempValue = String.join(",", itemConvertList);
        } else {
            tempValue = String.valueOf(value);
            if (value instanceof String) {
                tempValue = "'" + value + "'";
            }
        }

        String sql;
        if (Objects.equals(SqlOperation.IN, operation) || Objects.equals(SqlOperation.NOT_IN, operation) ) {
            sql = columnName + operateWithSpace + "(" + tempValue + ")";
        } else {
            sql = columnName + operateWithSpace + tempValue;
        }

        if (addAnd) {
            return "and " + sql;
        } else {
            return sql;
        }
    }

    /**
     * ------------------------ 传入集合 ----------------------------
     */
    public static String condition(String columnName, SqlOperation operation, String[] valueList) {
        return condition(columnName, operation, Arrays.asList(valueList), true);
    }

    public static String condition(String columnName, SqlOperation operation, String[] valueList, boolean addAnd) {
        return condition(columnName, operation, Arrays.asList(valueList), addAnd);
    }

    public static String condition(String columnName, SqlOperation operation, Collection<Object> valueList, boolean addAnd) {
        if (CollectionUtils.isEmpty(valueList)) {
            log.error("<ConditionGenerator> you input empty value list for column name = {}", columnName);
            return "";
        }

        String operateWithSpace = chooseSqlOperate(operation);
        if (Objects.equals(operation, SqlOperation.IN) || Objects.equals(operation, SqlOperation.NOT_IN) ) {
            // ok
        } else {
            throw new RuntimeException(String.format("When your value type is Collection, SqlOperation must is 'in', 'not in'. your operation = %s", operateWithSpace));
        }

        Set<String> dataSet = valueList.stream().filter(Objects::nonNull).map(value -> {
            if (value instanceof String) {
                return "'" + value + "'";
            } else {
                return String.valueOf(value);
            }
        }).collect(Collectors.toSet());
        String tempContent = columnName + operateWithSpace + "(" + String.join(",", dataSet) + ") ";

        if (addAnd) {
            return "and " + tempContent;
        } else {
            return tempContent;
        }
    }

    /**
     * 映射, 工厂方法.
     * @param operation 操作符
     * @return 条件符号
     */
    private static String chooseSqlOperate(SqlOperation operation) {
        switch (operation) {
            case EQ:
                return " = ";
            case NOT_EQ:
                return " != ";
            case GT:
                return " > ";
            case GTE:
                return " >= ";
            case LT:
                return " < ";
            case LTE:
                return " <= ";
            case IN:
                return " in ";
            case NOT_IN:
                return " not in ";
            default:
                throw new RuntimeException("not found this operate = " + operation);
        }
    }


}
