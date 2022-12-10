package com.neko233.sql.lightrail.condition;

import java.util.List;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public class OnCondition implements Condition {

    private StringBuilder onSqlBuilder = new StringBuilder("");

    private OnCondition() {
    }

    public static OnCondition builder() {
        return new OnCondition();
    }

    @Override
    public String build() {
        return onSqlBuilder.toString();
    }

    public OnCondition equalsTo(String tableAlias, String columnName, Object value) {
        isFirstParams();
        onSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" = ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public OnCondition equalsTo(String columnName, Object value) {
        isFirstParams();
        onSqlBuilder.append(columnName.trim()).append(" = ").append(Condition.toSqlValueByType(value));
        return this;
    }

    private void isFirstParams() {
        if (onSqlBuilder.length() == 0) {
            return;
        }
        onSqlBuilder.append(" and ");
    }

    public OnCondition in(String columnName, List<Object> valueList) {
        isFirstParams();
        onSqlBuilder.append(columnName.trim()).append(" in ( ");
        appendForEach(valueList);
        onSqlBuilder.append(" )");
        return this;
    }

    public OnCondition in(String tableAlias, String columnName, List<Object> valueList) {
        isFirstParams();
        onSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" in ( ");
        appendForEach(valueList);
        onSqlBuilder.append(" )");
        return this;
    }

    private void appendForEach(List<Object> valueList) {
        for (Object value : valueList) {
            onSqlBuilder.append(Condition.toSqlValueByType(value));
            onSqlBuilder.append(",");
        }
        onSqlBuilder.deleteCharAt(onSqlBuilder.length() - 1);
    }

    public OnCondition notIn(String columnName, List<Object> valueList) {
        isFirstParams();
        onSqlBuilder.append(columnName.trim()).append(" not in ( ");
        appendForEach(valueList);
        onSqlBuilder.append(" )");
        return this;
    }

    public OnCondition notIn(String tableAlias, String columnName, List<Object> valueList) {
        isFirstParams();
        onSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" not in ( ");
        appendForEach(valueList);
        onSqlBuilder.append(" )");
        return this;
    }

    public OnCondition isNull(String columnName) {
        isFirstParams();
        onSqlBuilder.append(columnName.trim()).append(" is null");
        return this;
    }

    public OnCondition isNull(String tableAlias, String columnName) {
        isFirstParams();
        onSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" is null");
        return this;
    }

    public OnCondition isNotNull(String columnName) {
        isFirstParams();
        onSqlBuilder.append(columnName.trim()).append(" is not null");
        return this;
    }

    public OnCondition isNotNull(String tableAlias, String columnName) {
        isFirstParams();
        onSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" is not null");
        return this;
    }

    public OnCondition between(String columnName, Object startValue, Object endValue) {
        isFirstParams();
        onSqlBuilder.append(columnName.trim()).append(" between(").append(startValue).append(",").append(endValue).append(")");
        return this;
    }

    public OnCondition between(String tableAlias, String columnName, Object startValue, Object endValue) {
        isFirstParams();
        onSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" between(").append(startValue).append(",").append(endValue).append(")");
        return this;
    }

    public OnCondition like(String columnName, Object value) {
        isFirstParams();
        onSqlBuilder.append(columnName.trim()).append(" like ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public OnCondition like(String tableAlias, String columnName, Object value) {
        isFirstParams();
        onSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" like ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public OnCondition greaterThan(String columnName, Object value) {
        isFirstParams();
        onSqlBuilder.append(columnName.trim()).append(" > ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public OnCondition greaterThan(String tableAlias, String columnName, Object value) {
        isFirstParams();
        onSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" > ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public OnCondition greaterThanOrEquals(String columnName, Object value) {
        isFirstParams();
        onSqlBuilder.append(columnName.trim()).append(" >= ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public OnCondition greaterThanOrEquals(String tableAlias, String columnName, Object value) {
        isFirstParams();
        onSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" >= ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public OnCondition lessThan(String columnName, Object value) {
        isFirstParams();
        onSqlBuilder.append(columnName.trim()).append(" < ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public OnCondition lessThan(String tableAlias, String columnName, Object value) {
        isFirstParams();
        onSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" < ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public OnCondition lessThanOrEquals(String columnName, Object value) {
        isFirstParams();
        onSqlBuilder.append(columnName.trim()).append(" <= ").append(Condition.toSqlValueByType(value));
        return this;
    }

    public OnCondition lessThanOrEquals(String tableAlias, String columnName, Object value) {
        isFirstParams();
        onSqlBuilder.append(tableAlias.trim()).append(".").append(columnName.trim()).append(" <= ").append(Condition.toSqlValueByType(value));
        return this;
    }

}
