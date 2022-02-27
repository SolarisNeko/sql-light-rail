package com.neko.lightrail.builder;

import com.neko.lightrail.condition.Condition;
import com.neko.lightrail.exception.SqlLightRailException;
import com.neko.lightrail.util.CamelCaseUtil;
import com.neko.lightrail.util.ReflectUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

    public InsertSqlBuilder values(Long insertTimes) {
        StringBuilder stringBuilder = new StringBuilder("(");
        for (int i = 0; i < sql.getColumns().size(); i++) {
            stringBuilder.append("?,");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(")");
        String placeholderTemplate = stringBuilder.toString();
        for (long i = 0; i < insertTimes - 1; i++) {
            stringBuilder.append(", ").append(placeholderTemplate);
        }
        sql.getValues().add(stringBuilder.toString());
        return this;
    }

    public <T> InsertSqlBuilder values(List<T> valueList) {
        if (CollectionUtils.isEmpty(valueList)) {
            throw new SqlLightRailException("Must set values! ");
        }
        T data = valueList.get(0);
        List<String> fieldNames = ReflectUtil.getFieldNames(data);
        resetColumnNames(fieldNames);

        List<String> valueSqlList = valueList.stream()
            .map(object -> {
                StringBuilder valueSql = new StringBuilder();
                valueSql.append("(");
                for (String fieldName : fieldNames) {
                    Object fieldValue = ReflectUtil.getFieldValueByNameShortly(object, fieldName);
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

    private void resetColumnNames(List<String> fieldNames) {
        sql.setColumns(new ArrayList());
        // db.column
        for (String fieldName : fieldNames) {
            sql.getColumns().add(CamelCaseUtil.getBigCamelLowerName(fieldName));
        }
    }

    public InsertSqlBuilder insertColumns(Class<?> clazz) {
        List<Field> allFields = ReflectUtil.getAllFields(clazz);
        List<String> columnNames = allFields.stream()
            .map(Field::getName)
            .map(CamelCaseUtil::getBigCamelLowerName)
            .sorted()
            .collect(toList());
        sql.setColumns(columnNames);
        return this;
    }
}
