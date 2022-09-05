package com.neko233.lightrail.builder;

import com.neko233.lightrail.condition.SetCondition;
import com.neko233.lightrail.condition.WhereCondition;
import com.neko233.lightrail.exception.SqlLightRailException;

import java.util.Optional;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public class UpdateSqlBuilder extends SqlBuilder {

    public UpdateSqlBuilder(String tableName) {
        super(tableName);
    }

    @Override
    public String build() {
        checkUpdateNecessaryParams(sql);
        return "Update " + sql.getTableList().get(0) + " "
            + Optional.ofNullable(sql.getSet()).orElse("")
            + Optional.ofNullable(sql.getWhere()).orElse("")
            ;
    }

    private void checkUpdateNecessaryParams(Sql sql) {
        if (sql.getSet() == null) {
            throw new SqlLightRailException("Update SQL must set 'Set' update content ! ");
        }
        if (sql.getWhere() == null) {
            throw new SqlLightRailException("Update SQL must set 'Where' condition ! ");
        }
    }

    public UpdateSqlBuilder set(String set) {
        sql.setSet("Set " + set);
        return this;
    }

    public UpdateSqlBuilder set(SetCondition set) {
        sql.setSet(set.build());
        return this;
    }

    public UpdateSqlBuilder where(WhereCondition where) {
        sql.setWhere(where.build());
        return this;
    }
}
