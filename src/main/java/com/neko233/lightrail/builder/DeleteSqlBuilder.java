package com.neko233.lightrail.builder;

import com.neko233.lightrail.condition.WhereCondition;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

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
        return "Delete From " + sql.getTableList().get(0)
            + (StringUtils.isBlank(sql.getWhere()) ?
            "" : sql.getWhere());
    }

    public DeleteSqlBuilder where(WhereCondition condition) {
        sql.setWhere(condition.build());
        return this;
    }
}
