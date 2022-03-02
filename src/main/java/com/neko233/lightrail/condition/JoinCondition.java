package com.neko233.lightrail.condition;

/**
 * @author SolarisNeko
 * Date on 2022-02-21
 **/
public class JoinCondition implements Condition {

    private String table = null;
    private String on = null;

    private JoinCondition() {
    }

    public static JoinCondition builder() {
        return new JoinCondition();
    }

    @Override
    public String build() {
        if (table == null) {
            return "";
        }
        if (on == null) {
            return "";
        }
        return "JOIN " + table + " ON " + on + " ";
    }

    public JoinCondition join(String tableName) {
        table = tableName;
        return this;
    }

    public JoinCondition on(OnCondition on) {
        if (on == null) {
            this.on = on.build();
        }
        this.on += " and " + on.build();
        return this;
    }

    public JoinCondition on(String... sql) {
        String onConditions = String.join(" and ", sql);
        if (on == null) {
            on = onConditions;
        } else {
            on = on + " and " + onConditions;
        }
        return this;
    }
}
