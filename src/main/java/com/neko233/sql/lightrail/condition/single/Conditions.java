package com.neko233.sql.lightrail.condition.single;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public class Conditions {

    public static WhereCondition where() {
        return WhereCondition.builder();
    }

    public static OrderByCondition orderBy() {
        return OrderByCondition.builder();
    }

    public static GroupByCondition groupBy() {
        return GroupByCondition.builder();
    }

    public static GroupByCondition groupByWithHaving(HavingCondition havingCondition) {
        GroupByCondition groupByCondition = GroupByCondition.builder();
        groupByCondition.setHavingCondition(havingCondition);
        return groupByCondition;
    }

    public static HavingCondition having() {
        return HavingCondition.builder();
    }

    public static SetCondition set() {
        return SetCondition.builder();
    }
}
