package com.neko.lightrail;

import com.neko.lightrail.builder.DeleteSqlBuilder;
import com.neko.lightrail.builder.InsertSqlBuilder;
import com.neko.lightrail.builder.SelectSqlBuilder;
import com.neko.lightrail.builder.UpdateSqlBuilder;

/**
 *
 *
 * @author SolarisNeko
 * @date 2022-02-20
 */
public class SqlLightRail {

    public static InsertSqlBuilder insertTable(String tableName) {
        return new InsertSqlBuilder(tableName);
    }

    public static DeleteSqlBuilder deleteTable(String tableName) {
        return new DeleteSqlBuilder(tableName.trim());
    }

    public static UpdateSqlBuilder updateTable(String tableName) {
        return new UpdateSqlBuilder(tableName.trim());
    }

    public static SelectSqlBuilder selectTable(String tableName) {
        return new SelectSqlBuilder(tableName.trim());
    }

    /**
     * 需要手动指定 table 别名
     */
    public static SelectSqlBuilder selectTable(String... tableName) {
        return new SelectSqlBuilder(tableName);
    }

    /**
     * 子查询
     */
    public static SelectSqlBuilder selectSubTable(SelectSqlBuilder innerSelectBuilder) {
        return new SelectSqlBuilder(" ( " + innerSelectBuilder.build() + " ) ");
    }

}
