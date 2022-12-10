package com.neko233.sql.lightrail.condition;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public class OrderByCondition implements Condition {

    private StringBuilder orderBySqlBuilder = new StringBuilder();

    private OrderByCondition() {

    }

    public static OrderByCondition builder() {
        return new OrderByCondition();
    }

    @Override
    public String build() {
        // 为了美观, 逗号后面都有空格, 所以 - 2
        return " ORDER BY " + orderBySqlBuilder.substring(0, orderBySqlBuilder.length() - 2);
    }

    public OrderByCondition orderByAsc(String... columnNames) {
        for (String columnName : columnNames) {
            orderBySqlBuilder.append(columnName).append(" ASC, ");
        }
        return this;
    }

    public OrderByCondition orderByDesc(String... columnNames) {
        for (String columnName : columnNames) {
            orderBySqlBuilder.append(columnName).append(" DESC, ");
        }
        return this;
    }


}
