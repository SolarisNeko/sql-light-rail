package com.neko233.sql.lightrail.builder;

import com.neko233.sql.lightrail.condition.Condition;
import com.neko233.sql.lightrail.condition.OnDuplicateUpdateCondition;
import com.neko233.sql.lightrail.exception.SqlLightRailException;
import com.neko233.sql.lightrail.util.CamelCaseUtil;
import com.neko233.sql.lightrail.util.ReflectUtil;
import com.neko233.sql.lightrail.util.SqlColumnUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        String onDuplicateUpdateCond = isOnDuplicateUpdate ? sqlContext.getSet() : "";
        return "INSERT INTO " + this.sqlContext.getTableList().get(0) + "("
                + String.join(", ", this.sqlContext.getColumnNameList()) + ") Values "
                + String.join(", ", this.sqlContext.getColumnValueList())
                + onDuplicateUpdateCond
                ;
    }


    public InsertSqlBuilder columnNames(String... columns) {
        List<String> columnNames = Arrays.stream(columns).collect(toList());
        sqlContext.setColumnNameList(columnNames);
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
        sqlContext.getColumnValueList().add("(" + singleRowValues + ")");
        return this;
    }

    public InsertSqlBuilder values(String valueString) {
        sqlContext.getColumnValueList().add(valueString);
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
        for (int i = 0; i < sqlContext.getColumnNameList().size(); i++) {
            stringBuilder.append("?,");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(")");
        String placeholderTemplate = stringBuilder.toString();
        for (long i = 0; i < insertTimes - 1; i++) {
            stringBuilder.append(", ").append(placeholderTemplate);
        }
        sqlContext.getColumnValueList().add(stringBuilder.toString());
        return this;
    }

    public <T> InsertSqlBuilder values(List<T> insertValueList) {
        if (CollectionUtils.isEmpty(insertValueList)) {
            throw new SqlLightRailException("Must set values! ");
        }
        T data = insertValueList.get(0);


        Map<String, Field> columnNameToFieldMap = SqlColumnUtil.getColumnName2FieldMap(data.getClass());

        // 如果 columns 为空, 才会自动生成
        if (CollectionUtils.isEmpty(this.sqlContext.getColumnNameList())) {
            List<String> columnNames = columnNameToFieldMap.keySet().stream().sorted().collect(toList());
            this.addColumnNameList(columnNames);
        }

        List<String> valueSqlList = insertValueList.stream()
                .map(object -> {
                    StringBuilder valueSql = new StringBuilder();
                    valueSql.append("(");
                    for (String columnName : this.sqlContext.getColumnNameList()) {
                        Object fieldValue = ReflectUtil.getFieldValue(object, columnNameToFieldMap.get(columnName));
                        String sqlValueByType = Condition.toSqlValueByType(fieldValue);
                        valueSql.append(sqlValueByType).append(", ");
                    }
                    valueSql.delete(valueSql.length() - 2, valueSql.length());
                    valueSql.append(")");
                    return valueSql.toString();
                })
                .filter(StringUtils::isNotBlank)
                .collect(toList());

        // 一次 API 生成一次局部 SQL
        for (String value : valueSqlList) {
            sqlContext.getColumnValueList().add(value);
        }
        return this;
    }

    /**
     * @param columnNames 列名
     */
    private void addColumnNameList(List<String> columnNames) {
        // db.column
        List<String> columnNameList = sqlContext.getColumnNameList();
        columnNameList.addAll(columnNames);
    }

    public InsertSqlBuilder columnNames(Class<?> clazz) {
        List<Field> allFields = ReflectUtil.getAllFields(clazz);
        List<String> columnNames = allFields.stream()
                .map(Field::getName)
                .map(CamelCaseUtil::getBigCamelLowerName)
                .sorted()
                .collect(toList());
        sqlContext.setColumnNameList(columnNames);
        return this;
    }

    public InsertSqlBuilder onDuplicateUpdate(OnDuplicateUpdateCondition condition) {
        isOnDuplicateUpdate = true;
        sqlContext.setSet(condition.build());
        return this;
    }


}
