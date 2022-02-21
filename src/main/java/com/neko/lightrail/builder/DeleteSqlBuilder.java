package com.neko.lightrail.builder;

import com.neko.lightrail.condition.WhereCondition;

import java.util.Optional;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
public class DeleteSqlBuilder extends SqlBuilder {

    public DeleteSqlBuilder(String tableName) {
        super(tableName);
    }

    @Override
    public String build() {
        return buildDeleteSql();
    }

    private String buildDeleteSql() {
        return "Delete From " + sql.getTableList().get(0) + " "
            + Optional.ofNullable(sql.getWhere()).orElse("") + " "
            ;
    }


    public DeleteSqlBuilder where(WhereCondition condition) {
        sql.setWhere(condition.build());
        return this;
    }
}
