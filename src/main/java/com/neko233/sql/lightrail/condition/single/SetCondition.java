package com.neko233.sql.lightrail.condition.single;

import com.neko233.sql.lightrail.condition.Condition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public class SetCondition implements Condition {

    private final StringBuilder setSqlBuilder = new StringBuilder();

    private final Map<String, Object> kvMap = new HashMap<>();

    private SetCondition() {
    }

    public static SetCondition builder() {
        return new SetCondition();
    }

    @Override
    public String build() {
        setSqlBuilder.append("Set ");

        List<String> setConditionList = kvMap.entrySet().stream()
                .map(e -> e.getKey() + " = " + Condition.toSqlValueByType(e.getValue()))
                .collect(Collectors.toList());
        setSqlBuilder.append(String.join(", ", setConditionList));
        return setSqlBuilder.toString();
    }

    public SetCondition equalsTo(String columnName, Object value) {
        kvMap.put(columnName, value);
        return this;
    }

}
