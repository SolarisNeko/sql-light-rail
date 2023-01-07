package com.neko233.sql.lightrail.condition.single;

import com.neko233.sql.lightrail.condition.Condition;
import com.neko233.sql.lightrail.exception.SqlLightRailException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
public class SelectCondition implements Condition {

    private List<String> selectColumns = new ArrayList<>();

    private SelectCondition() {
    }

    public static SelectCondition builder() {
        return new SelectCondition();
    }

    @Override
    public String build() {
        return String.join(", ", selectColumns);
    }

    public SelectCondition column(String column) {
        selectColumns.add(column);
        return this;
    }

    /**
     *
     * @param column 可以是 id, 也可以是 count(id)
     * @param alias 别名
     * @return Builder
     */
    public SelectCondition column(String column, String alias) {
        if (alias == null) {
            throw new SqlLightRailException("Alias can't be null! column = " + column + ", alias = "+ alias);
        }
        selectColumns.add(column + " as " + Condition.toSqlValueByType(alias));
        return this;
    }


}
