package com.neko.sqlchain.condition;

import java.util.List;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
public class OrderByCondition {

    private StringBuilder orderBySqlBuilder = new StringBuilder(" ORDER BY ");

    private OrderByCondition() {

    }

    public static OrderByCondition builder() {
        return new OrderByCondition();
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

    public String build() {
        // 为了美观, 逗号后面都有空格, 所以 - 2
        return orderBySqlBuilder.substring(0, orderBySqlBuilder.length() - 2);
    }
}
