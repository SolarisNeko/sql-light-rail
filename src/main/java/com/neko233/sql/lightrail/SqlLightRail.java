package com.neko233.sql.lightrail;

import com.neko233.sql.lightrail.sql_builder.DeleteSqlBuilder;
import com.neko233.sql.lightrail.sql_builder.InsertSqlBuilder;
import com.neko233.sql.lightrail.sql_builder.SelectSqlBuilder;
import com.neko233.sql.lightrail.sql_builder.UpdateSqlBuilder;

/**
 * 后面统一使用 {@link SqlBuilder233}, 预计 v1.2.0 废弃
 *
 * @author SolarisNeko
 * Date on 2022-02-20
 */
@Deprecated
public class SqlLightRail {


    @Deprecated
    public static InsertSqlBuilder insertTable(String tableName) {
        return SqlBuilder233.insertTable(tableName);
    }

    @Deprecated
    public static DeleteSqlBuilder deleteTable(String tableName) {
        return SqlBuilder233.deleteTable(tableName);
    }

    @Deprecated
    public static UpdateSqlBuilder updateTable(String tableName) {
        return SqlBuilder233.updateTable(tableName);
    }

    /**
     * 将 Class 进行 ORM 转换成 Select SQL.
     *
     * @param entity 数据库实体对象, 遵守 CamelCase(驼峰命名法) 这个约定。
     * @return SelectSqlBuilder
     */
    @Deprecated
    public static SelectSqlBuilder selectTable(Class<?> entity) {
        return SqlBuilder233.selectTable(entity);
    }

    @Deprecated
    public static SelectSqlBuilder selectTable(String tableName,
                                               Class<?> tablePojo) {
        return SqlBuilder233.selectTable(tableName, tablePojo);
    }


    @Deprecated
    public static SelectSqlBuilder selectTable(String tableName) {
        return SqlBuilder233.selectTable(tableName);
    }


    @Deprecated
    public static SelectSqlBuilder selectTable(String... tableNameArray) {
        return SqlBuilder233.selectTable(tableNameArray);
    }

    /**
     * 子查询
     *
     * @param innerSelectBuilder builder
     * @return SelectSqlBuilder
     */
    @Deprecated
    public static SelectSqlBuilder selectSubTable(SelectSqlBuilder innerSelectBuilder,
                                                  String subTableName) {
        return SqlBuilder233.selectSubTable(innerSelectBuilder, subTableName);
    }


    /**
     * 自动生成插入模板
     *
     * @param clazz       类结构
     * @param insertCount 插入次数
     * @return insert builder
     */
    @Deprecated
    public static InsertSqlBuilder generateInsertTemplateAuto(Class<?> clazz,
                                                              Long insertCount) {
        return SqlBuilder233.generateInsertTemplateAuto(clazz, insertCount);
    }

    /**
     * 自动生成 Insert 的占位符模板
     *
     * @param tableName   表名
     * @param clazz       生成 select 模板
     * @param insertTimes 次数
     * @return InsertSqlBuilder
     */
    @Deprecated
    public static InsertSqlBuilder generateInsertTemplateAuto(String tableName,
                                                              Class<?> clazz,
                                                              Long insertTimes) {
        return SqlBuilder233.generateInsertTemplateAuto(tableName, clazz, insertTimes);
    }
}
