package com.neko.lightrail.condition;

/**
 * @author SolarisNeko
 * @date 2022-02-20
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

    public SetCondition equalsTo(String columnName, String value) {
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
