package com.neko233.sql.lightrail.condition.single;

import com.neko233.sql.lightrail.common.KvTemplate;
import com.neko233.sql.lightrail.condition.Condition;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OnDuplicateUpdateCondition implements Condition {

    private final StringBuilder builder = new StringBuilder();
    private final Map<String, Object> kvMap = new HashMap<>(1);
    private final List<String> valuesColumnList = new ArrayList<>(1);

    private OnDuplicateUpdateCondition() {

    }

    @Override
    public String build() {
        // 定制 update
        List<String> setConditionList = kvMap.entrySet().stream()
                .map(e -> e.getKey() + " = " + Condition.toSqlValueByType(e.getValue()))
                .collect(Collectors.toList());
        builder.append(String.join(", ", setConditionList));

        if (CollectionUtils.isNotEmpty(valuesColumnList)) {
            // 取 insert values 值
            String valuesPart = valuesColumnList.stream()
                    .map(columnName -> KvTemplate.builder("`${columnName}` = values(`${columnName}`)")
                            .put("columnName", columnName)
                            .build())
                    .collect(Collectors.joining(", "));
            if (builder.length() == 0) {
                builder.append(valuesPart);
            } else {
                builder.append(", " + valuesPart);
            }
        }
        return " On Duplicate Key Update " + builder.toString();
    }

    public static OnDuplicateUpdateCondition builder() {
        return new OnDuplicateUpdateCondition();
    }

    public OnDuplicateUpdateCondition equalsTo(String columnName, Object value) {
        kvMap.put(columnName, value);
        return this;
    }

    public OnDuplicateUpdateCondition updateValue(String columnName) {
        if (StringUtils.isBlank(columnName)) {
            return this;
        }
        valuesColumnList.add(columnName.trim());
        return this;
    }
}
