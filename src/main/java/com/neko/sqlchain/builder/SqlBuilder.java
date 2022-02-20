package com.neko.sqlchain.builder;

import com.neko.sqlchain.pojo.SqlString;

/**
 *
 *
 * @author SolarisNeko
 * @date 2022-02-20
 */
public abstract class SqlBuilder<T> {

    protected SqlString sqlString = new SqlString();

    public SqlBuilder(String tableName) {
        sqlString.setTable(tableName);
    }


    abstract String printSql();



}
