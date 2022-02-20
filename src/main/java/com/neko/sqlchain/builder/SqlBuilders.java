package com.neko.sqlchain.builder;

/**
 *
 *
 * @author SolarisNeko
 * @date 2022-02-20
 */
public class SqlBuilders {

    public static SqlBuilder insertBuilder(String tableName) {
        return null;
    }

    public static SqlBuilder deleteBuilder(String tableName) {
        return null;
    }

    public static SqlBuilder updateBuilder(String tableName) {
        return null;
    }

    public static SelectSqlBuilder selectBuilder(String tableName) {
        return new SelectSqlBuilder(tableName);
    }

}
