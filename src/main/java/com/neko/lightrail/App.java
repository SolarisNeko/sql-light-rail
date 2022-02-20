package com.neko.lightrail;

import com.neko.lightrail.builder.SqlBuilders;
import com.neko.lightrail.condition.Conditions;
import com.neko.lightrail.condition.WhereCondition;

import java.util.Arrays;

/**
 * CRUD Demo 已迁移到  {@link AppTest}
 * 此处只保留 Select 的常见用法, 详细请看 AppTest
 *
 * @author SolarisNeko
 */
public class App {

    public static void main(String[] args) {
        selectSmallDemo();
//        selectAllUseTest()
    }

    private static void selectSmallDemo() {
        // 最常见的 demo
        String build = SqlBuilders.selectBuilder("user")
            .select("id", "name")
            .where(
                WhereCondition.builder()
                    .equalsTo("id", 1)
            )
            .build();
        System.out.println(build);
    }


    /**
     * Select 所有用法列举
     */
    private static void selectAllUseTest() {
        // Table <- columns, condition
        String selectSql = SqlBuilders.selectBuilder("user")
            .select("id")
            .where(Conditions.where()
                .equalsTo("id", 1)
                .in("name", Arrays.asList("neko", "doge"))
                .isNull("job")
                .isNotNull("createTime")
                .between("age", 25, 35.2)
                .like("alias", "dog%")
                .greaterThan("age", 27)
                .lessThan("age", 32)
                .greaterThanOrEquals("age", 25)
                .lessThanOrEquals("age", 32)
            ).orderBy(
                Conditions.orderBy()
                    .orderByAsc("a", "b")
            ).groupBy(
                Conditions.groupByWithHaving(Conditions.having().equalsTo("id", 1))
                    .groupBy("id", "name")
            ).limit(0, 10)
            .build();
        System.out.println(selectSql);
    }


}
