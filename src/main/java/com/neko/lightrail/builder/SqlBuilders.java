package com.neko.lightrail.builder;

/**
 * 快捷入口
 * @author SolarisNeko
 * Date on 2022-03-01
 */
public class SqlBuilders {

    public static SelectSqlBuilder select(String tableName) {
        return new SelectSqlBuilder(tableName);
    }

    public static InsertSqlBuilder insert(String tableName) {
        return new InsertSqlBuilder(tableName);
    }

    public static UpdateSqlBuilder update(String tableName) {
        return new UpdateSqlBuilder(tableName);
    }

    public static DeleteSqlBuilder delete(String tableName) {
        return new DeleteSqlBuilder(tableName);
    }

}
