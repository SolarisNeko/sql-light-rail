package com.neko233.lightrail.builder;

import com.neko233.lightrail.condition.Condition;
import com.neko233.lightrail.condition.OnDuplicateUpdateCondition;
import com.neko233.lightrail.exception.SqlLightRailException;
import com.neko233.lightrail.util.CamelCaseUtil;
import com.neko233.lightrail.util.ReflectUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public class InsertSqlBuilder extends SqlBuilder {

    private Boolean isOnDuplicateUpdate = false;

    public InsertSqlBuilder(String tableName) {
        super(tableName);
    }

    @Override
    public String build() {
        String onDuplicateUpdateCond = isOnDuplicateUpdate ? sql.getSet() : "";
        return "INSERT INTO " + this.sql.getTableList().get(0) + "("
                + String.join(", ", this.sql.getColumns()) + ") Values "
                + String.join(", ", this.sql.getRowValueList())
                + onDuplicateUpdateCond
                ;
    }


    public InsertSqlBuilder columnNames(String... columns) {
        sql.setColumns(Arrays.asList(columns));
        return this;
    }

    public InsertSqlBuilder singleRowValue(Object... oneRowDataList) {
        singleRowValue(Arrays.asList(oneRowDataList));
        return this;
    }

    /**
     * 单行的值, 需要和 columnName 对应上
     *
     * @param oneRowDataList 单行 Object
     * @return this
     */
    public InsertSqlBuilder singleRowValue(List<Object> oneRowDataList) {
        List<String> valueList = oneRowDataList.stream()
                .map(Condition::toSqlValueByType)
                .collect(toList());
        String singleRowValues = String.join(",", valueList);
        sql.getRowValueList().add("(" + singleRowValues + ")");
        return this;
    }

    public InsertSqlBuilder values(String valueString) {
        sql.getRowValueList().add(valueString);
        return this;
    }

    /**
     * 生成占位符 ?
     *
     * @param insertTimes 生成多少次
     * @return 带占位符的内容
     */
    public InsertSqlBuilder generateReplacement(Long insertTimes) {
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
        sql.getRowValueList().add(stringBuilder.toString());
        return this;
    }

    public <T> InsertSqlBuilder values(List<T> insertValueList) {
        if (CollectionUtils.isEmpty(insertValueList)) {
            throw new SqlLightRailException("Must set values! ");
        }
        T data = insertValueList.get(0);
        List<String> fieldNames = ReflectUtil.getFieldNames(data);
        resetColumnNames(fieldNames);

        List<String> valueSqlList = insertValueList.stream()
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
        for (String value : valueSqlList) {
            sql.getRowValueList().add(value);
        }
        return this;
    }

    private void resetColumnNames(List<String> fieldNames) {
        sql.setColumns(new ArrayList());
        // db.column
        for (String fieldName : fieldNames) {
            sql.getColumns().add(CamelCaseUtil.getBigCamelLowerName(fieldName));
        }
    }

    public InsertSqlBuilder columnNames(Class<?> clazz) {
        List<Field> allFields = ReflectUtil.getAllFields(clazz);
        List<String> columnNames = allFields.stream()
                .map(Field::getName)
                .map(CamelCaseUtil::getBigCamelLowerName)
                .sorted()
                .collect(toList());
        sql.setColumns(columnNames);
        return this;
    }

    public InsertSqlBuilder onDuplicateUpdate(OnDuplicateUpdateCondition condition) {
        isOnDuplicateUpdate = true;
        sql.setSet(condition.build());
        return this;
    }


}
