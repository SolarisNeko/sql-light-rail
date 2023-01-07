package com.neko233.sql.lightrail.condition.single;


import com.neko233.sql.lightrail.condition.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public class WhereCondition implements Condition {

    public static final String WHERE_PREFIX = " WHERE ";
    List<String> whereCondition = new ArrayList<>();

    private WhereCondition() {
    }

    public static WhereCondition builder() {
        return new WhereCondition();
    }

    @Override
    public String build() {
        return WHERE_PREFIX + String.join(" and ", whereCondition);
    }

    public WhereCondition equalsTo(String tableAlias, String columnName, Object value) {
        StringBuilder append = new StringBuilder().append(tableAlias.trim()).append(".").append(columnName.trim())
            .append(" = ").append(Condition.toSqlValueByType(value));
        whereCondition.add(append.toString());
        return this;
    }

    public WhereCondition equalsTo(String columnName, Object value) {
        StringBuilder append = new StringBuilder().append(columnName.trim())
            .append(" = ").append(Condition.toSqlValueByType(value));
        whereCondition.add(append.toString());
        return this;
    }


    public WhereCondition in(String columnName, List<Object> valueList) {
        StringBuilder append = new StringBuilder().append(columnName.trim()).append(" in ( ");
        appendForEach(append, valueList);
        append.append(" )");
        whereCondition.add(append.toString());
        return this;
    }

    public WhereCondition in(String tableAlias, String columnName, List<Object> valueList) {
        StringBuilder append = new StringBuilder().append(tableAlias.trim()).append(".").append(columnName.trim())
            .append(" in ( ");
        appendForEach(append, valueList);
        append.append(" )");
        whereCondition.add(append.toString());
        return this;
    }

    private void appendForEach(StringBuilder append, List<Object> valueList) {
        for (Object value : valueList) {
            append.append(Condition.toSqlValueByType(value));
            append.append(",");
        }
        append.deleteCharAt(append.length() - 1);
    }

    public WhereCondition notIn(String columnName, List<Object> valueList) {
        StringBuilder append = new StringBuilder().append(columnName.trim()).append(" not in ( ");
        appendForEach(append, valueList);
        append.append(" )");
        whereCondition.add(append.toString());
        return this;
    }

    public WhereCondition notIn(String tableAlias, String columnName, List<Object> valueList) {
        StringBuilder append = new StringBuilder().append(tableAlias.trim()).append(".").append(columnName.trim()).append(" not in ( ");
        appendForEach(append, valueList);
        append.append(" )");
        whereCondition.add(append.toString());
        return this;
    }

    public WhereCondition isNull(String columnName) {
        whereCondition.add(columnName.trim() + " is null");
        return this;
    }

    public WhereCondition isNull(String tableAlias, String columnName) {
        whereCondition.add(tableAlias.trim() + "." + columnName.trim() + " is null");
        return this;
    }

    public WhereCondition isNotNull(String columnName) {
        whereCondition.add(columnName.trim() + " is not null");
        return this;
    }

    public WhereCondition isNotNull(String tableAlias, String columnName) {
        whereCondition.add(tableAlias.trim() + "." + columnName.trim() + " is not null");
        return this;
    }

    public WhereCondition between(String columnName, Object startValue, Object endValue) {
        whereCondition.add(columnName.trim() + " between(" + startValue + "," + endValue + ")");
        return this;
    }

    public WhereCondition between(String tableAlias, String columnName, Object startValue, Object endValue) {
        whereCondition.add(tableAlias.trim() + "."  + columnName.trim()
            + " between(" + startValue + "," + endValue + ")");
        return this;
    }

    public WhereCondition like(String columnName, Object value) {
        whereCondition.add(columnName.trim() + " like " + Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition like(String tableAlias, String columnName, Object value) {
        whereCondition.add(tableAlias.trim() + "." + columnName.trim() + " like " + Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition greaterThan(String columnName, Object value) {
        whereCondition.add(columnName.trim() + " > " + Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition greaterThan(String tableAlias, String columnName, Object value) {
        whereCondition.add(tableAlias.trim() + "." + columnName.trim() + " > " + Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition greaterThanOrEquals(String columnName, Object value) {
        whereCondition.add(columnName.trim() + " >= " + Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition greaterThanOrEquals(String tableAlias, String columnName, Object value) {
        whereCondition.add(tableAlias.trim() + "." + columnName.trim() + " >= " + Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition lessThan(String columnName, Object value) {
        whereCondition.add(columnName.trim() + " < " + Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition lessThan(String tableAlias, String columnName, Object value) {
        whereCondition.add(tableAlias.trim() + "." + columnName.trim() + " < " + Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition lessThanOrEquals(String columnName, Object value) {
        whereCondition.add(columnName.trim() + " <= " + Condition.toSqlValueByType(value));
        return this;
    }

    public WhereCondition lessThanOrEquals(String tableAlias, String columnName, Object value) {
        whereCondition.add(tableAlias.trim() + "." + columnName.trim() + " <= " + Condition.toSqlValueByType(value));
        return this;
    }

}
