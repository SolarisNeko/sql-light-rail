package com.neko.lightrail.builder;

/**
 *
 *
 * @author SolarisNeko
 * @date 2022-02-20
 */
public abstract class SqlBuilder {

    protected Sql sql = new Sql();

    public SqlBuilder(String tableName) {
        sql.setTable(tableName);
    }

    /**
     * 输出 SQL
     * @return 组装好的 SQL String
     */
    abstract public String build();

}

