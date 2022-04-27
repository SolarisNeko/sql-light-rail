package com.neko233.lightrail.condition;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public class SetCondition implements Condition {

    private StringBuilder setSqlBuilder = new StringBuilder("Set ");

    private SetCondition() {
    }

    public static SetCondition builder() {
        return new SetCondition();
    }

    @Override
    public String build() {
        return setSqlBuilder.toString();
    }

    public SetCondition equalsTo(String columnName, Object value) {
        if (setSqlBuilder.toString().equals("Set ")) {
            setSqlBuilder.append(columnName)
                .append(" = ")
                .append(Condition.toSqlValueByType(value));
            return this;
        }
        setSqlBuilder.append(", ")
            .append(columnName)
            .append(" = ")
            .append(Condition.toSqlValueByType(value));
        return this;
    }

}
