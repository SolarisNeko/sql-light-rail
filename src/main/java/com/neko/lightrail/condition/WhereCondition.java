package com.neko.lightrail.condition;

import jdk.nashorn.internal.objects.annotations.Where;

import java.util.List;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
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

    public WhereCondition equalsTo(String tableAlias, String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" = ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition equalsTo(String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(columnName.trim()).append(" = ").append(Condition.toSqlValueByType(value));
        return this;
    }

    private void isFirstParams() {
        if (whereSqlBuilder.length() <= EXISTS_STRING_NUMBER) {
            return;
        }
        whereSqlBuilder.append(" and ");
    }

    public WhereCondition in(String columnName, List<Object> valueList) {
        isFirstParams();
        whereSqlBuilder.append(columnName.trim()).append(" in ( ");
        appendForEach(valueList);
        whereSqlBuilder.append(" )");
        return this;
    }

    public WhereCondition in(String tableAlias, String columnName, List<Object> valueList) {
        isFirstParams();
        whereSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" in ( ");
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
        whereSqlBuilder.append(columnName.trim()).append(" not in ( ");
        appendForEach(valueList);
        whereSqlBuilder.append(" )");
        return this;
    }

    public WhereCondition notIn(String tableAlias, String columnName, List<Object> valueList) {
        isFirstParams();
        whereSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" not in ( ");
        appendForEach(valueList);
        whereSqlBuilder.append(" )");
        return this;
    }

    public WhereCondition isNull(String columnName) {
        isFirstParams();
        whereSqlBuilder.append(columnName.trim()).append(" is null");
        return this;
    }

    public WhereCondition isNull(String tableAlias, String columnName) {
        isFirstParams();
        whereSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" is null");
        return this;
    }

    public WhereCondition isNotNull(String columnName) {
        isFirstParams();
        whereSqlBuilder.append(columnName.trim()).append(" is not null");
        return this;
    }

    public WhereCondition isNotNull(String tableAlias, String columnName) {
        isFirstParams();
        whereSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" is not null");
        return this;
    }

    public WhereCondition between(String columnName, Object startValue, Object endValue) {
        isFirstParams();
        whereSqlBuilder.append(columnName.trim()).append(" between(").append(startValue).append(",").append(endValue).append(")");
        return this;
    }

    public WhereCondition between(String tableAlias, String columnName, Object startValue, Object endValue) {
        isFirstParams();
        whereSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" between(").append(startValue).append(",").append(endValue).append(")");
        return this;
    }

    public WhereCondition like(String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(columnName.trim()).append(" like ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition like(String tableAlias, String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" like ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition greaterThan(String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(columnName.trim()).append(" > ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition greaterThan(String tableAlias, String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" > ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition greaterThanOrEquals(String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(columnName.trim()).append(" >= ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition greaterThanOrEquals(String tableAlias, String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" >= ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition lessThan(String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(columnName.trim()).append(" < ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition lessThan(String tableAlias, String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" < ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition lessThanOrEquals(String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(columnName.trim()).append(" <= ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition lessThanOrEquals(String tableAlias, String columnName, Object value) {
        isFirstParams();
        whereSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" <= ").append(Condition.toSqlValueByType(value));
        return this;
    }

}
