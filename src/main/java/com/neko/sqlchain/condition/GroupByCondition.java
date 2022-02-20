package com.neko.sqlchain.condition;

import java.util.Optional;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
public class GroupByCondition {

    private HavingCondition havingCondition = null;

    private StringBuilder groupBySqlBuilder = new StringBuilder("GROUP BY ");

    private GroupByCondition() {
    }

    public static GroupByCondition builder() {
        return new GroupByCondition();
    }

    public void setHavingCondition(HavingCondition havingCondition) {
        this.havingCondition = havingCondition;
    }

    public GroupByCondition groupBy(String... columnNames) {
        for (String columnName : columnNames) {
            groupBySqlBuilder.append(columnName).append(", ");
        }
        return this;
    }

    public String build() {
        if (havingCondition == null) {
            return groupBySqlBuilder.substring(0, groupBySqlBuilder.length() - 2);
        }
        return groupBySqlBuilder.substring(0, groupBySqlBuilder.length() - 2)
            + havingCondition.build();
    }


}
