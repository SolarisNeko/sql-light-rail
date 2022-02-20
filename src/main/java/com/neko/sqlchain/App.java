package com.neko.sqlchain;

import com.neko.sqlchain.condition.Conditions;
import com.neko.sqlchain.condition.WhereCondition;
import com.neko.sqlchain.builder.SelectSqlBuilder;
import com.neko.sqlchain.builder.SqlBuilders;

public class App {
    public static void main(String[] args) {
        // Table <- columns, condition
        SelectSqlBuilder builder = SqlBuilders.selectBuilder("users");
        String table = builder.select("id")
            .where(Conditions.where()
//                .equalsTo("id", 1)
//                .in("name", Arrays.asList("neko", "doge"))
//                .isNull("job")
//                .isNotNull("createTime")
//                .between("age", 25, 35.2)
//                .like("alias", "dog%")
//                .greaterThan("age", 27)
//                .lessThan("age", 32)
//                .greaterThanOrEquals("age", 25)
//                .lessThanOrEquals("age", 32)
            ).orderBy(
                Conditions.orderBy()
                    .orderByAsc("a", "b")
            ).groupBy(
                Conditions.groupByWithHaving(Conditions.having().equalsTo("id", 1))
                    .groupBy("id", "name")
            )
            .printSql();
        System.out.println(table);
    }
}
