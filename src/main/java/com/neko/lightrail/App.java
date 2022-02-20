package com.neko.lightrail;

import com.neko.lightrail.builder.SqlBuilders;
import com.neko.lightrail.condition.Conditions;
import com.neko.lightrail.pojo.User;
import com.neko.lightrail.pojo.UserExt;

import java.util.Arrays;

public class App {

    public static void main(String[] args) {
//        selectSqlTest();
//        deleteSqlTest();
//        updateSqlTest();
        insertSqlTest();
//        insertRecursiveSqlTest();
    }

    /**
     * Entity 是单个对象
     */
    private static void insertSqlTest() {
        String insertSql = SqlBuilders.insertBuilder("user")
            .insertColumns("id", "name")
            .values(Arrays.asList(
                new User(10, "demo1"),
                new User(20, "demo2")
            )).build();
        System.out.println(insertSql);
    }

    /**
     * Entity 是继承体系
     */
    private static void insertRecursiveSqlTest() {
        String insertSql = SqlBuilders.insertBuilder("user")
            .insertColumns("id", "name", "age")
            .values(Arrays.asList(
                new UserExt(10, "demo1", 18),
                new UserExt(20, "demo2", 30)
            )).build();
        System.out.println(insertSql);
    }

    private static void deleteSqlTest() {
        String deleteSql = SqlBuilders.deleteBuilder("user")
            .where(Conditions.where()
                .equalsTo("id", 1)
            )
            .build();
        System.out.println(deleteSql);
    }

    private static void updateSqlTest() {
        String updateSql = SqlBuilders.updateBuilder("user")
            .set(Conditions.set()
                .equalsTo("name", "neko")
            )
            .where(Conditions.where()
                .equalsTo("id", 1)
            )
            .build();
        System.out.println(updateSql);
    }

    private static void selectSqlTest() {
        // Table <- columns, condition
        String selectSql = SqlBuilders.selectBuilder("user")
            .select("id")
            .where(Conditions.where()
                .equalsTo("id", 1)
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
            ).limitByPage(1, 10)
            .build();
        System.out.println(selectSql);
    }
}
