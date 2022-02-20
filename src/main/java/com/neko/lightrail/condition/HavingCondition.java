package com.neko.lightrail.condition;

import java.util.List;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
public class HavingCondition implements Condition {

    private StringBuilder havingSqlBuilder = new StringBuilder("HAVING 1 = 1");

    private HavingCondition() {
    }

    public static HavingCondition builder() {
        return new HavingCondition();
    }

    @Override
    public String build() {
        return havingSqlBuilder.toString();
    }

    public HavingCondition equalsTo(String columnName, Object value) {
        // TODO value 还需要判断类型
        havingSqlBuilder.append(" and ")
            .append(columnName)
            .append(" = ")
            .append(value);
        return this;
    }

    public HavingCondition in(String columnName, List<Object> valueList) {
        havingSqlBuilder.append(" and ")
            .append(columnName)
            .append(" in ( ");
        appendForEach(valueList);
        havingSqlBuilder.deleteCharAt(havingSqlBuilder.length() - 1);
        havingSqlBuilder.append(" )");
        return this;
    }

    private void appendForEach(List<Object> valueList) {
        for (Object value : valueList) {
            // TODO value 还需要判断类型 String/Number
            havingSqlBuilder.append(value);
            havingSqlBuilder.append(",");
        }
    }

    public HavingCondition notIn(String columnName, List<Object> valueList) {
        havingSqlBuilder.append(" and ")
            .append(columnName)
            .append(" not in ( ");
        appendForEach(valueList);
        havingSqlBuilder.deleteCharAt(havingSqlBuilder.length() - 1);
        havingSqlBuilder.append(" )");
        return this;
    }

    public HavingCondition isNull(String columnName) {
        havingSqlBuilder.append(" and ")
            .append(columnName)
            .append(" is null");
        return this;
    }

    public HavingCondition isNotNull(String columnName) {
        havingSqlBuilder.append(" and ")
            .append(columnName)
            .append(" is not null");
        return this;
    }

    public HavingCondition between(String columnName, Object startValue, Object endValue) {
        havingSqlBuilder.append(" and ")
            .append(columnName)
            .append(" between(")
            .append(startValue)
            .append(",")
            .append(endValue)
            .append(")");
        return this;
    }

    public HavingCondition like(String columnName, Object value) {
        havingSqlBuilder.append(" and ")
            .append(columnName)
            .append(" like ")
            .append(toSqlValueByType(value));
        return this;
    }

    /**
     * 根据 value 的类型, 返回 SQL 中的语句
     */
    private static String toSqlValueByType(Object value) {
        if (value instanceof CharSequence) {
            return "'" + value + "'";
        }
        return value.toString();
    }

    /**
     * TODO value 如果是 String, 应该给一个 Log WARN
     */
    public HavingCondition greaterThan(String columnName, Object value) {
        havingSqlBuilder.append(" and ")
            .append(columnName)
            .append(" > ")
            .append(toSqlValueByType(value));
        return this;
    }

    public HavingCondition greaterThanOrEquals(String columnName, Object value) {
        havingSqlBuilder.append(" and ")
            .append(columnName)
            .append(" >= ")
            .append(toSqlValueByType(value));
        return this;
    }

    public HavingCondition lessThan(String columnName, Object value) {
        havingSqlBuilder.append(" and ")
            .append(columnName)
            .append(" < ")
            .append(toSqlValueByType(value));
        return this;
    }

    public HavingCondition lessThanOrEquals(String columnName, Object value) {
        havingSqlBuilder.append(" and ")
            .append(columnName)
            .append(" <= ")
            .append(toSqlValueByType(value));
        return this;
    }



}
