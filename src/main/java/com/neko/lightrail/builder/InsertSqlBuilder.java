package com.neko.lightrail.builder;

import com.neko.lightrail.condition.Condition;
import com.neko.lightrail.condition.Conditions;
import com.neko.lightrail.exception.SqlLightRailException;
import com.neko.lightrail.util.ReflectUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
public class InsertSqlBuilder extends SqlBuilder {

    public InsertSqlBuilder(String tableName) {
        super(tableName);
    }

    @Override
    public String build() {
        return "INSERT INTO " + sql.getTable() + "("
            + String.join(", ", sql.getColumns()) + ") Values "
            + String.join(", ", sql.getValues())
            ;
    }


    public InsertSqlBuilder insertColumns(String... columns) {
        sql.setColumns(Arrays.asList(columns));
        return this;
    }

    public InsertSqlBuilder values(String valueString) {
        sql.getValues().add(valueString);
        return this;
    }

    public InsertSqlBuilder values(List<Object> valueList) {
        List<String> insertColumns = sql.getColumns();
        if (CollectionUtils.isEmpty(insertColumns)) {
            throw new SqlLightRailException("Must set columns which you insert before inserting into table by valueList ! ");
        }

        List<String> valueSqlList = valueList.stream()
            .map(object -> {
                StringBuilder valuesBuilder = new StringBuilder();
                valuesBuilder.append("( ");
                for (String insertColumn : insertColumns) {
                    Object fieldValue = ReflectUtil.getFieldValueByNameShortly(object, insertColumn);
                    valuesBuilder.append(Condition.toSqlValueByType(fieldValue)).append(", ");
                }
                valuesBuilder.deleteCharAt(valuesBuilder.length() - 2);
                valuesBuilder.append(")");
                return valuesBuilder.toString();
            })
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toList());
        // 一次 API 生成一次局部 SQL
        sql.getValues().add(String.join(",", valueSqlList));
        return this;
    }
}
