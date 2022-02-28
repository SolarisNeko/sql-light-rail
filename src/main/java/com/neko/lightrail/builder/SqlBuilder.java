package com.neko.lightrail.builder;

import java.util.Collections;
import java.util.List;

/**
 *
 *
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public abstract class SqlBuilder {

    protected Sql sql = new Sql();

    public SqlBuilder(String tableName) {
        sql.setTableList(Collections.singletonList(tableName));
    }

    public SqlBuilder(List<String> tableNameList) {
        sql.setTableList(tableNameList);
    }

    /**
     * 输出 SQL
     * @return 组装好的 SQL String
     */
    abstract public String build();



}

