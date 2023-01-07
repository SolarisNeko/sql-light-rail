package com.neko233.sql.lightrail.builder;

import com.neko233.sql.lightrail.condition.Condition;
import com.neko233.sql.lightrail.condition.single.GroupByCondition;
import com.neko233.sql.lightrail.condition.single.JoinCondition;
import com.neko233.sql.lightrail.condition.single.OrderByCondition;
import com.neko233.sql.lightrail.condition.single.WhereCondition;
import com.neko233.sql.lightrail.exception.SqlLightRailException;
import com.neko233.sql.lightrail.util.CamelCaseUtil;
import com.neko233.sql.lightrail.util.ReflectUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public class SelectSqlBuilder extends SqlBuilder {

    private static final String LOG_PREFIX_TIPS = "[SelectSqlBuilder] ";

    private static final String WHERE_PREFIX = "where";

    /**
     * 应该指定一个可大小写的选择参数
     *
     * @return String
     */
    @Override
    public String build() {
        checkSelectNecessaryParams(sqlContext);
        List<String> selectList = sqlContext.getSelect().stream()
            .filter(Objects::nonNull)
            .map(fieldName -> {
                String alias = sqlContext.getAliasMap().get(fieldName);
                if (alias != null) {
                    return fieldName + " as " + Condition.toSqlValueByType(alias);
                }
                return CamelCaseUtil.getBigCamelLowerName(fieldName);
            })
            .collect(toList());
        //
        return "SELECT " + String.join(", ", selectList)
            + " FROM " + String.join(",", sqlContext.getTableList())
            + Optional.ofNullable(sqlContext.getWhere()).orElse("")
            + Optional.ofNullable(sqlContext.getOrderBy()).orElse("")
            + Optional.ofNullable(sqlContext.getGroupBy()).orElse("")
            + Optional.ofNullable(sqlContext.getLimit()).orElse("")
            + Optional.ofNullable(sqlContext.getJoin()).orElse("")
            ;
    }

    /**
     * 如果没有传 tableName, 默认使用 Class 的 Lower CamelCase 小驼峰。
     *
     * @param tablePojo 符合驼峰大小写的 Pojo, 例如: LoginSum to login_sum
     */
    public SelectSqlBuilder(Class<?> tablePojo) {
        this(CamelCaseUtil.getBigCamelLowerName(tablePojo.getSimpleName()), tablePojo);
    }

    public SelectSqlBuilder(String tableName, Class<?> tablePojo) {
        super(tableName);
        List<String> fieldNames = ReflectUtil.getFieldNames(tablePojo);
        sqlContext.setSelect(fieldNames);
    }


    public SelectSqlBuilder(String tableName) {
        super(tableName);
    }

    public SelectSqlBuilder(String... tables) {
        super(Arrays.stream(tables).map(String::trim).collect(toList()));
    }

    public SelectSqlBuilder(List<String> tableNameList) {
        super(tableNameList);
    }

    private static void checkSelectNecessaryParams(SqlContext sqlContext) {
        List<String> selectList = sqlContext.getSelect().stream()
            .filter(StringUtils::isNotBlank)
            .collect(toList());
        if (CollectionUtils.isEmpty(selectList)) {
            throw new SqlLightRailException("Must set 'select' in SQL");
        }
    }

    /**
     * Select Entity's all field with 'Camel Case(驼峰命名法)' as same as table's columns.
     * @param entity as same as table's columns by rule 'Camel Case'
     * @return builder
     */
    public SelectSqlBuilder select(Class<?> entity) {
        List<Field> allFields = ReflectUtil.getAllFields(entity);
        List<String> columnNames = allFields.stream()
            .map(field -> CamelCaseUtil.getBigCamelLowerName(field.getName()))
            .collect(toList());
        sqlContext.setSelect(columnNames);
        return this;
    }

    public SelectSqlBuilder select(String... columns) {
        sqlContext.setSelect(Arrays.asList(columns));
        return this;
    }

    public SelectSqlBuilder where(WhereCondition condition) {
        sqlContext.setWhere(condition.build());
        return this;
    }

    public SelectSqlBuilder where(String whereSql) {
        if (!whereSql.toLowerCase(Locale.ROOT).trim().startsWith(WHERE_PREFIX)) {
            sqlContext.setWhere(" " + WHERE_PREFIX + " " + whereSql);
        } else {
            sqlContext.setWhere(" " + whereSql);
        }
        return this;
    }

    public SelectSqlBuilder groupBy(String... columns) {
        sqlContext.setGroupBy(" Group By " + String.join(", ", columns));
        return this;
    }

    public SelectSqlBuilder groupBy(String column) {
        sqlContext.setGroupBy(" Group By " + column);
        return this;
    }

    public SelectSqlBuilder groupBy(GroupByCondition condition) {
        sqlContext.setGroupBy(condition.build());
        return this;
    }

    public SelectSqlBuilder orderBy(String orderBySql) {
        sqlContext.setOrderBy(orderBySql);
        return this;
    }

    public SelectSqlBuilder orderBy(OrderByCondition condition) {
        sqlContext.setOrderBy(condition.build());
        return this;
    }

    public SelectSqlBuilder limit(Integer start, Integer step) {
        if (start == null) {
            return this;
        }
        if (step == null) {
            return this;
        }
        sqlContext.setLimit(" LIMIT " + start + ", " + step + " ");
        return this;
    }

    /**
     * pageNum 从 1 开始
     * pageSize 从 1 开始
     *
     * @param pageNum 开始页数
     * @param pageSize 分页
     * @return SelectSqlBuilder
     */
    public SelectSqlBuilder limitByPage(Integer pageNum, Integer pageSize) {
        if (pageNum == null) {
            return this;
        }
        if (pageSize == null) {
            return this;
        }

        if (pageNum <= 0) {
            throw new SqlLightRailException(LOG_PREFIX_TIPS + "your pageNum <= 0! can't use. Must set >= 1");
        }
        if (pageSize <= 0) {
            throw new SqlLightRailException(LOG_PREFIX_TIPS + "your pageSize <= 0! can't use. Must set >= 1");
        }
        int startStep = pageNum - 1;
        sqlContext.setLimit(" LIMIT " + startStep * pageSize + ", " + (startStep * pageSize + pageSize) + " ");
        return this;
    }


    public SelectSqlBuilder join(JoinCondition join) {
        sqlContext.setJoin(join.build());
        return this;
    }

    public SelectSqlBuilder join(String join) {
        sqlContext.setJoin(join);
        return this;
    }

    public SelectSqlBuilder alias(Map<String, String> aliasMap) {
        sqlContext.getAliasMap().putAll(aliasMap);
        return this;
    }



}
