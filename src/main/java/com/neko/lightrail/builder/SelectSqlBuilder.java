package com.neko.lightrail.builder;

import com.neko.lightrail.condition.Condition;
import com.neko.lightrail.condition.GroupByCondition;
import com.neko.lightrail.condition.JoinCondition;
import com.neko.lightrail.condition.OrderByCondition;
import com.neko.lightrail.condition.WhereCondition;
import com.neko.lightrail.exception.SqlLightRailException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
public class SelectSqlBuilder extends SqlBuilder {


    public SelectSqlBuilder(String tableName) {
        super(tableName);
    }

    public SelectSqlBuilder(String... tables) {
        super(Arrays.stream(tables).map(String::trim).collect(toList()));
    }

    public SelectSqlBuilder(List<String> tableNameList) {
        super(tableNameList);
    }

    /**
     * TODO 应该指定一个可大小写的选择参数
     *
     * @return
     */
    @Override
    public String build() {
        checkSelectNecessaryParams(sql);
        List<String> selectList = sql.getSelect().stream()
                .filter(Objects::nonNull)
                .map(t -> {
                    String alias = sql.getAliasMap().get(t);
                    if (alias == null) {
                        return t;
                    }
                    return t + " as " + Condition.toSqlValueByType(alias);
                })
                .collect(toList());
        return "SELECT " + String.join(", ", selectList)
                + " FROM " + String.join(",", sql.getTableList()) + " "
                + Optional.ofNullable(sql.getWhere()).orElse("")
                + Optional.ofNullable(sql.getOrderBy()).orElse("")
                + Optional.ofNullable(sql.getGroupBy()).orElse("")
                + Optional.ofNullable(sql.getLimit()).orElse("")
                + Optional.ofNullable(sql.getJoin()).orElse("")
                ;
    }

    private static void checkSelectNecessaryParams(Sql sql) {
        List<String> selectList = sql.getSelect().stream()
                .filter(StringUtils::isNotBlank)
                .collect(toList());
        if (CollectionUtils.isEmpty(selectList)) {
            throw new SqlLightRailException("Must set 'select' in SQL");
        }

    }

    public SelectSqlBuilder select(String... columns) {
        sql.setSelect(Arrays.asList(columns));
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
     *
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


    public SelectSqlBuilder join(JoinCondition join) {
        sql.setJoin(join.build());
        return this;
    }

    public SelectSqlBuilder join(String join) {
        sql.setJoin(join);
        return this;
    }

    public SelectSqlBuilder alias(Map<String, String> aliasMap) {
        sql.getAliasMap().putAll(aliasMap);
        return this;
    }
}
