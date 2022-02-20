package com.neko.sqlchain.builder;

import com.neko.sqlchain.condition.GroupByCondition;
import com.neko.sqlchain.condition.OrderByCondition;
import com.neko.sqlchain.condition.WhereCondition;
import com.neko.sqlchain.exception.SqlChainException;
import com.neko.sqlchain.pojo.SqlString;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.Optional;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
public class SelectSqlBuilder extends SqlBuilder {


    public SelectSqlBuilder(String tableName) {
        super(tableName);
    }

    /**
     * TODO 应该指定一个可大小写的选择参数
     *
     * @return
     */
    @Override
    public String printSql() {
        return buildSelectSql();
    }

    private String buildSelectSql() {
        checkNecessaryParams(sqlString);
        return "SELECT " + sqlString.getSelect()
            + " FROM " + sqlString.getTable() + " "
            + Optional.ofNullable(sqlString.getWhere()).orElse("") + " "
            + Optional.ofNullable(sqlString.getOrderBy()).orElse("") + " "
            + Optional.ofNullable(sqlString.getGroupBy()).orElse("") + " "
            ;
    }

    private static void checkNecessaryParams(SqlString sqlString) {
        String select = sqlString.getSelect();
        String table = sqlString.getTable();

        if (StringUtils.isEmpty(select)) {
            throw new SqlChainException("SelectSqlBuilder can't get select columns... | select = " + select);
        }
        if (StringUtils.isEmpty(table)) {
            throw new SqlChainException("SelectSqlBuilder can't get select columns... | table = " + table);
        }
    }

    public SelectSqlBuilder select(String columns) {
        sqlString.setSelect(columns);
        return this;
    }

    public SelectSqlBuilder where(WhereCondition condition) {
        sqlString.setWhere(condition.build());
        return this;
    }

    public SelectSqlBuilder where(String whereSql) {
        if (!whereSql.toLowerCase(Locale.ROOT).trim().startsWith("where")) {
            sqlString.setWhere(" where " + whereSql);
        } else {
            sqlString.setWhere(" " + whereSql);
        }
        return this;
    }

    public SelectSqlBuilder groupBy(String columns) {
        sqlString.setGroupBy(columns);
        return this;
    }

    public SelectSqlBuilder groupBy(GroupByCondition condition) {
        sqlString.setGroupBy(condition.build());
        return this;
    }

    public SelectSqlBuilder orderBy(String columns) {
        sqlString.setOrderBy(columns);
        return this;
    }

    public SelectSqlBuilder orderBy(OrderByCondition condition) {
        sqlString.setOrderBy(condition.build());
        return this;
    }

    public SelectSqlBuilder limit(Integer pageNum, Integer pageSize) {
        if (pageNum == null) {
            return this;
        }
        if (pageSize == null) {
            return this;
        }
        sqlString.setLimit(" limit " + pageNum + "," + pageSize + " ");
        return this;
    }


}
