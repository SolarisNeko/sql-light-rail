package com.neko233.lightrail.condition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OnDuplicateUpdateCondition implements Condition {

    private final StringBuilder builder = new StringBuilder();
    private final Map<String, Object> kvMap = new HashMap<>();

    private OnDuplicateUpdateCondition() {

    }

    @Override
    public String build() {
        builder.append(" On Duplicate Key Update ");

        List<String> setConditionList = kvMap.entrySet().stream()
                .map(e -> e.getKey() + " = " + Condition.toSqlValueByType(e.getValue()))
                .collect(Collectors.toList());
        builder.append(String.join(", ", setConditionList));
        return builder.toString();
    }

    public static OnDuplicateUpdateCondition builder() {
        return new OnDuplicateUpdateCondition();
    }

    public OnDuplicateUpdateCondition equalsTo(String columnName, Object value) {
        kvMap.put(columnName, value);
        return this;
    }
}
