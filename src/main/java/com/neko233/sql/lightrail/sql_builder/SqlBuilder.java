package com.neko233.sql.lightrail.sql_builder;

import java.util.Collections;
import java.util.List;

/**
 *
 *
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public abstract class SqlBuilder {

    protected SqlContext sqlContext = new SqlContext();

    public SqlBuilder(String tableName) {
        sqlContext.setTableList(Collections.singletonList(tableName));
    }

    public SqlBuilder(List<String> tableNameList) {
        sqlContext.setTableList(tableNameList);
    }

    /**
     * 输出 SQL
     * @return 组装好的 SQL String
     */
    abstract public String build();



}

