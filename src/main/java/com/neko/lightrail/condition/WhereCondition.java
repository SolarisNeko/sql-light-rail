package com.neko.lightrail.condition;

import java.util.List;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
public class WhereCondition implements Condition {

    public static final Integer EXISTS_STRING_NUMBER = 6;
    private StringBuilder whereSqlBuilder = new StringBuilder("WHERE ");

    private WhereCondition() {
    }

    public static WhereCondition builder() {
        return new WhereCondition();
    }

    @Override
    public String build() {
        return whereSqlBuilder.toString();
    }

    public WhereCondition equalsTo(String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder
            .append(columnName)
            .append(" = ")
            .append(Condition.toSqlValueByType(value));
        return this;
    }

    private void isFirstParams() {
        if (whereSqlBuilder.length() == EXISTS_STRING_NUMBER) {
            return;
        }
        whereSqlBuilder.append(" and ");
    }

    public WhereCondition in(String columnName, List<Object> valueList) {
        isFirstParams();
        whereSqlBuilder.append(columnName).append(" in ( ");
        appendForEach(valueList);
        whereSqlBuilder.append(" )");
        return this;
    }

    private void appendForEach(List<Object> valueList) {
        for (Object value : valueList) {
            whereSqlBuilder.append(Condition.toSqlValueByType(value));
            whereSqlBuilder.append(",");
        }
        whereSqlBuilder.deleteCharAt(whereSqlBuilder.length() - 1);
    }

    public WhereCondition notIn(String columnName, List<Object> valueList) {
        isFirstParams();
        whereSqlBuilder.append(columnName).append(" not in ( ");
        appendForEach(valueList);
        whereSqlBuilder.append(" )");
        return this;
    }

    public WhereCondition isNull(String columnName) {
        isFirstParams();
        whereSqlBuilder.append(columnName).append(" is null");
        return this;
    }

    public WhereCondition isNotNull(String columnName) {
        isFirstParams();
        whereSqlBuilder.append(columnName).append(" is not null");
        return this;
    }

    public WhereCondition between(String columnName, Object startValue, Object endValue) {
        isFirstParams();
        whereSqlBuilder.append(columnName)
            .append(" between(")
            .append(startValue)
            .append(",")
            .append(endValue)
            .append(")");
        return this;
    }

    public WhereCondition like(String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(columnName).append(" like ").append(Condition.toSqlValueByType(value));
        return this;
    }

    /**
     * value 如果是 String, 应该给一个 Log WARN
     */
    public WhereCondition greaterThan(String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(columnName).append(" > ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition greaterThanOrEquals(String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(columnName).append(" >= ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition lessThan(String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(columnName).append(" < ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition lessThanOrEquals(String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(columnName).append(" <= ").append(Condition.toSqlValueByType(value));
        return this;
    }

}
