package com.neko233.sql.lightrail.builder;

import com.neko233.sql.lightrail.condition.single.SetCondition;
import com.neko233.sql.lightrail.condition.single.WhereCondition;
import com.neko233.sql.lightrail.exception.SqlLightRailException;

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
        checkUpdateNecessaryParams(sqlContext);
        return "Update " + sqlContext.getTableList().get(0) + " "
            + Optional.ofNullable(sqlContext.getSet()).orElse("")
            + Optional.ofNullable(sqlContext.getWhere()).orElse("")
            ;
    }

    private void checkUpdateNecessaryParams(SqlContext sqlContext) {
        if (sqlContext.getSet() == null) {
            throw new SqlLightRailException("Update SQL must set 'Set' update content ! ");
        }
        if (sqlContext.getWhere() == null) {
            throw new SqlLightRailException("Update SQL must set 'Where' condition ! ");
        }
    }

    public UpdateSqlBuilder set(String set) {
        sqlContext.setSet("Set " + set);
        return this;
    }

    public UpdateSqlBuilder set(SetCondition set) {
        sqlContext.setSet(set.build());
        return this;
    }

    public UpdateSqlBuilder where(WhereCondition where) {
        sqlContext.setWhere(where.build());
        return this;
    }
}
