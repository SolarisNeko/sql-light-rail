package com.neko.lightrail;

import com.neko.lightrail.builder.DeleteSqlBuilder;
import com.neko.lightrail.builder.InsertSqlBuilder;
import com.neko.lightrail.builder.SelectSqlBuilder;
import com.neko.lightrail.builder.UpdateSqlBuilder;
import com.neko.lightrail.util.CamelCaseUtil;

/**
 *
 *
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public class SqlLightRail {


    public static InsertSqlBuilder generateInsertTemplate(Class<?> clazz, Long insertTimes) {
        return generateInsertTemplate(CamelCaseUtil.getBigCamelLowerName(clazz.getSimpleName()), clazz, insertTimes);
    }

    /**
     * 生成 Insert 的占位符模板
     * @param tableName 表名
     * @param clazz 生成 select 模板
     * @param insertTimes 次数
     * @return InsertSqlBuilder
     */
    public static InsertSqlBuilder generateInsertTemplate(String tableName, Class<?> clazz, Long insertTimes) {
        return new InsertSqlBuilder(tableName).insertColumns(clazz).values(insertTimes);
    }

    public static InsertSqlBuilder insertTable(String tableName) {
        return new InsertSqlBuilder(tableName);
    }

    public static DeleteSqlBuilder deleteTable(String tableName) {
        return new DeleteSqlBuilder(tableName.trim());
    }

    public static UpdateSqlBuilder updateTable(String tableName) {
        return new UpdateSqlBuilder(tableName.trim());
    }

    /**
     * 将 Class 进行 ORM 转换成 Select SQL.
     * ps: ClassName, FieldNames 需要遵守 CamelCase(驼峰命名法) 这个约定。
     * @param tablePojo camelCasePojo
     * @return SelectSqlBuilder
     */
    public static SelectSqlBuilder selectTable(Class tablePojo) {
        return new SelectSqlBuilder(tablePojo);
    }

    public static SelectSqlBuilder selectTable(String tableName, Class tablePojo) {
        return new SelectSqlBuilder(tableName, tablePojo);
    }


    public static SelectSqlBuilder selectTable(String tableName) {
        return new SelectSqlBuilder(tableName.trim());
    }


    public static SelectSqlBuilder selectTable(String... tableName) {
        return new SelectSqlBuilder(tableName);
    }

    /**
     * 子查询
     * @param innerSelectBuilder builder
     * @return SelectSqlBuilder
     */
    public static SelectSqlBuilder selectSubTable(SelectSqlBuilder innerSelectBuilder) {
        return new SelectSqlBuilder(" ( " + innerSelectBuilder.build() + " ) ");
    }

}
