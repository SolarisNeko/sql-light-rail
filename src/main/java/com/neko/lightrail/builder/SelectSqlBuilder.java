package com.neko.lightrail.builder;

import com.neko.lightrail.condition.GroupByCondition;
import com.neko.lightrail.condition.OrderByCondition;
import com.neko.lightrail.condition.WhereCondition;
import com.neko.lightrail.exception.SqlLightRailException;
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
    public String build() {
        checkSelectNecessaryParams(sql);
        return buildSelectSql();
    }

    private String buildSelectSql() {
        return "SELECT " + sql.getSelect()
            + " FROM " + sql.getTable() + " "
            + Optional.ofNullable(sql.getWhere()).orElse("")
            + Optional.ofNullable(sql.getOrderBy()).orElse("")
            + Optional.ofNullable(sql.getGroupBy()).orElse("")
            + Optional.ofNullable(sql.getLimit()).orElse("")
            ;
    }

    private static void checkSelectNecessaryParams(Sql sql) {
        String select = sql.getSelect();
        String table = sql.getTable();

        if (StringUtils.isEmpty(select)) {
            throw new SqlLightRailException("SelectSqlBuilder can't get select columns... | select = " + select);
        }
        if (StringUtils.isEmpty(table)) {
            throw new SqlLightRailException("SelectSqlBuilder can't get select columns... | table = " + table);
        }
    }

    public SelectSqlBuilder select(String... columns) {
        sql.setSelect(String.join(", ", columns));
        return this;
    }

    public SelectSqlBuilder where(WhereCondition condition) {
        sql.setWhere(condition.build());
        return this;
    }

    public SelectSqlBuilder where(String whereSql) {
        if (!whereSql.toLowerCase(Locale.ROOT).trim().startsWith("where")) {
            sql.setWhere(" where " + whereSql);
        } else {
            sql.setWhere(" " + whereSql);
        }
        return this;
    }

    public SelectSqlBuilder groupBy(String columns) {
        sql.setGroupBy(columns);
        return this;
    }

    public SelectSqlBuilder groupBy(GroupByCondition condition) {
        sql.setGroupBy(condition.build());
        return this;
    }

    public SelectSqlBuilder orderBy(String columns) {
        sql.setOrderBy(columns);
        return this;
    }

    public SelectSqlBuilder orderBy(OrderByCondition condition) {
        sql.setOrderBy(condition.build());
        return this;
    }

    public SelectSqlBuilder limit(Integer start, Integer step) {
        if (start == null) {
            return this;
        }
        if (step == null) {
            return this;
        }
        sql.setLimit(" LIMIT " + start + ", " + step + " ");
        return this;
    }

    /**
     * pageNum 从 0 开始
     * @param pageNum
     * @param pageSize
     * @return
     */
    public SelectSqlBuilder limitByPage(Integer pageNum, Integer pageSize) {
        if (pageNum == null) {
            return this;
        }
        if (pageSize == null) {
            return this;
        }
        sql.setLimit(" LIMIT " + pageNum * pageSize + ", " + (pageNum * pageSize + pageSize) + " ");
        return this;
    }


}
