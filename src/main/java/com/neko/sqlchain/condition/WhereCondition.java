package com.neko.sqlchain.condition;

import java.util.List;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
public class WhereCondition {

    private StringBuilder whereSqlBuilder = new StringBuilder("WHERE 1 = 1");

    private WhereCondition() {
    }

    public static WhereCondition builder() {
        return new WhereCondition();
    }

    public WhereCondition equalsTo(String columnName, Object value) {
        // TODO value 还需要判断类型
        whereSqlBuilder.append(" and ")
            .append(columnName)
            .append(" = ")
            .append(value);
        return this;
    }

    public WhereCondition in(String columnName, List<Object> valueList) {
        whereSqlBuilder.append(" and ")
            .append(columnName)
            .append(" in ( ");
        appendForEach(valueList);
        whereSqlBuilder.deleteCharAt(whereSqlBuilder.length() - 1);
        whereSqlBuilder.append(" )");
        return this;
    }

    private void appendForEach(List<Object> valueList) {
        for (Object value : valueList) {
            // TODO value 还需要判断类型 String/Number
            whereSqlBuilder.append(value);
            whereSqlBuilder.append(",");
        }
    }

    public WhereCondition notIn(String columnName, List<Object> valueList) {
        whereSqlBuilder.append(" and ")
            .append(columnName)
            .append(" not in ( ");
        appendForEach(valueList);
        whereSqlBuilder.deleteCharAt(whereSqlBuilder.length() - 1);
        whereSqlBuilder.append(" )");
        return this;
    }

    public String build() {
        return whereSqlBuilder.toString();
    }

    public WhereCondition isNull(String columnName) {
        whereSqlBuilder.append(" and ")
            .append(columnName)
            .append(" is null");
        return this;
    }

    public WhereCondition isNotNull(String columnName) {
        whereSqlBuilder.append(" and ")
            .append(columnName)
            .append(" is not null");
        return this;
    }

    public WhereCondition between(String columnName, Object startValue, Object endValue) {
        whereSqlBuilder.append(" and ")
            .append(columnName)
            .append(" between(")
            .append(startValue)
            .append(",")
            .append(endValue)
            .append(")");
        return this;
    }

    public WhereCondition like(String columnName, Object value) {
        whereSqlBuilder.append(" and ")
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
    public WhereCondition greaterThan(String columnName, Object value) {
        whereSqlBuilder.append(" and ")
            .append(columnName)
            .append(" > ")
            .append(toSqlValueByType(value));
        return this;
    }

    public WhereCondition greaterThanOrEquals(String columnName, Object value) {
        whereSqlBuilder.append(" and ")
            .append(columnName)
            .append(" >= ")
            .append(toSqlValueByType(value));
        return this;
    }

    public WhereCondition lessThan(String columnName, Object value) {
        whereSqlBuilder.append(" and ")
            .append(columnName)
            .append(" < ")
            .append(toSqlValueByType(value));
        return this;
    }

    public WhereCondition lessThanOrEquals(String columnName, Object value) {
        whereSqlBuilder.append(" and ")
            .append(columnName)
            .append(" <= ")
            .append(toSqlValueByType(value));
        return this;
    }

}
