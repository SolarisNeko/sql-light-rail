package com.neko233.sql.lightrail.builder;

import com.neko233.sql.lightrail.condition.WhereCondition;
import org.apache.commons.lang3.StringUtils;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public class DeleteSqlBuilder extends SqlBuilder {

    public DeleteSqlBuilder(String tableName) {
        super(tableName);
    }

    @Override
    public String build() {
        return "Delete From " + sqlContext.getTableList().get(0)
            + (StringUtils.isBlank(sqlContext.getWhere()) ?
            "" : sqlContext.getWhere());
    }

    public DeleteSqlBuilder where(WhereCondition condition) {
        sqlContext.setWhere(condition.build());
        return this;
    }
}
