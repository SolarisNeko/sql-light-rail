package com.neko.lightrail.builder;

import com.neko.lightrail.condition.Condition;
import com.neko.lightrail.exception.SqlLightRailException;
import com.neko.lightrail.util.CamelCaseUtil;
import com.neko.lightrail.util.ReflectUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
        return "INSERT INTO " + sql.getTableList().get(0) + "("
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

    public <T> InsertSqlBuilder values(List<T> valueList) {
        if (CollectionUtils.isEmpty(valueList)) {
            throw new SqlLightRailException("Must set values! ");
        }
        T data = valueList.get(0);
        List<String> fieldNames = ReflectUtil.getFieldNames(data);

        if (CollectionUtils.isEmpty(sql.getColumns())) {
            // db.column
            for (String fieldName : fieldNames) {
                sql.getColumns().add(CamelCaseUtil.getBigCamelLowerName(fieldName));
            }
        }

        List<String> valueSqlList = valueList.stream()
                .map(object -> {
                    StringBuilder valueSql = new StringBuilder();
                    valueSql.append("(");
                    for (String insertColumn : fieldNames) {
                        Object fieldValue = ReflectUtil.getFieldValueByNameShortly(object, insertColumn);
                        valueSql.append(Condition.toSqlValueByType(fieldValue)).append(", ");
                    }
                    valueSql.delete(valueSql.length() - 2, valueSql.length());
                    valueSql.append(")");
                    return valueSql.toString();
                })
                .filter(StringUtils::isNotBlank)
                .collect(toList());

        // 一次 API 生成一次局部 SQL
        sql.getValues().add(String.join(", ", valueSqlList));
        return this;
    }
}
