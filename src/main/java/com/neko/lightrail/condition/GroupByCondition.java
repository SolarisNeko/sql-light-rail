package com.neko.lightrail.condition;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public class GroupByCondition implements Condition {

    private HavingCondition havingCondition = null;

    private StringBuilder groupBySqlBuilder = new StringBuilder(" GROUP BY ");

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

    @Override
    public String build() {
        if (havingCondition == null) {
            return groupBySqlBuilder.substring(0, groupBySqlBuilder.length() - 2);
        }
        return groupBySqlBuilder.substring(0, groupBySqlBuilder.length() - 2) + " "
            + havingCondition.build();
    }


}
