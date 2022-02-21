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

    public static InsertSqlBuilder insertBuilder(String tableName) {
        return new InsertSqlBuilder(tableName);
    }

    public static DeleteSqlBuilder deleteBuilder(String tableName) {
        return new DeleteSqlBuilder(tableName.trim());
    }

    public static UpdateSqlBuilder updateBuilder(String tableName) {
        return new UpdateSqlBuilder(tableName.trim());
    }

    public static SelectSqlBuilder selectBuilder(String tableName) {
        return new SelectSqlBuilder(tableName.trim());
    }

    /**
     * 需要手动指定 table 别名
     */
    public static SelectSqlBuilder selectBuilder(String... tableName) {
        return new SelectSqlBuilder(tableName);
    }

    /**
     * 子查询
     */
    public static SelectSqlBuilder innerSelectBuilder(SelectSqlBuilder innerSelectBuilder) {
        return new SelectSqlBuilder(" ( " + innerSelectBuilder.build() + " ) ");
    }

}
