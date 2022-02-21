package com.neko.lightrail.builder;

/**
 *
 *
 * @author SolarisNeko
 * @date 2022-02-20
 */
public class SqlBuilders {

    public static InsertSqlBuilder insertBuilder(String tableName) {
        return new InsertSqlBuilder(tableName);
    }

    public static DeleteSqlBuilder deleteBuilder(String tableName) {
        return new DeleteSqlBuilder(tableName);
    }

    public static UpdateSqlBuilder updateBuilder(String tableName) {
        return new UpdateSqlBuilder(tableName);
    }

    public static SelectSqlBuilder selectBuilder(String tableName) {
        return new SelectSqlBuilder(tableName);
    }

    /**
     * 子查询
     */
    public static SelectSqlBuilder innerSelectBuilder(SelectSqlBuilder innerSelectBuilder) {
        return new SelectSqlBuilder(" ( " + innerSelectBuilder.build() + " ) ");
    }

}
