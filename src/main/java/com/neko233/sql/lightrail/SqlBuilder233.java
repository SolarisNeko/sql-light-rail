package com.neko233.sql.lightrail;

import com.neko233.sql.lightrail.sql_builder.DeleteSqlBuilder;
import com.neko233.sql.lightrail.sql_builder.InsertSqlBuilder;
import com.neko233.sql.lightrail.sql_builder.SelectSqlBuilder;
import com.neko233.sql.lightrail.sql_builder.UpdateSqlBuilder;
import com.neko233.sql.lightrail.util.CamelCaseUtil;

/**
 * 快捷入口
 * @author SolarisNeko
 * Date on 2022-03-01
 */
public class SqlBuilder233 {

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
     *
     * @param entity 数据库实体对象, 遵守 CamelCase(驼峰命名法) 这个约定。
     * @return SelectSqlBuilder
     */
    public static SelectSqlBuilder selectTable(Class<?> entity) {
        return new SelectSqlBuilder(entity);
    }

    public static SelectSqlBuilder selectTable(String tableName, Class<?> tablePojo) {
        return new SelectSqlBuilder(tableName, tablePojo);
    }


    public static SelectSqlBuilder selectTable(String tableName) {
        return new SelectSqlBuilder(tableName.trim());
    }


    public static SelectSqlBuilder selectTable(String... tableNameArray) {
        return new SelectSqlBuilder(tableNameArray);
    }

    /**
     * 子查询
     *
     * @param innerSelectBuilder builder
     * @return SelectSqlBuilder
     */
    public static SelectSqlBuilder selectSubTable(SelectSqlBuilder innerSelectBuilder, String subTableName) {
        return new SelectSqlBuilder("( " + innerSelectBuilder.build() + " ) " + subTableName + " ");
    }




    public static InsertSqlBuilder generateInsertTemplateAuto(Class<?> clazz, Long insertTimes) {
        return generateInsertTemplateAuto(CamelCaseUtil.getBigCamelLowerName(clazz.getSimpleName()), clazz, insertTimes);
    }

    /**
     * 自动生成 Insert 的占位符模板
     *
     * @param tableName   表名
     * @param clazz       生成 select 模板
     * @param insertTimes 次数
     * @return InsertSqlBuilder
     */
    public static InsertSqlBuilder generateInsertTemplateAuto(String tableName, Class<?> clazz, Long insertTimes) {
        return new InsertSqlBuilder(tableName).columnNames(clazz).generateReplacement(insertTimes);
    }
}
