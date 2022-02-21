package com.neko.lightrail;

import com.neko.lightrail.builder.SelectSqlBuilder;
import com.neko.lightrail.builder.SqlBuilders;
import com.neko.lightrail.condition.Conditions;
import com.neko.lightrail.condition.WhereCondition;
import com.neko.lightrail.pojo.User;
import com.neko.lightrail.pojo.UserExt;
import jdk.nashorn.internal.objects.annotations.Where;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author SolarisNeko
 * @date 2022-02-21
 */
public class AppTest {

    @Test
    public void innerSelectSqlTest() {
        SelectSqlBuilder innerBuilder = SqlBuilders.selectBuilder("inner_demo")
                .select("id", "name")
                .where(WhereCondition.builder()
                        .equalsTo("id", 1)
                );
        String select = SqlBuilders.innerSelectBuilder(innerBuilder)
                .select("id", "name")
                .build();
        String target = "SELECT id, name FROM  ( SELECT id, name FROM inner_demo WHERE id = 1 )  ";
        Assert.assertEquals(target, select);
    }

    /**
     * Entity 是单个对象
     */
    @Test
    public void insertSqlTest() {
        String insertSql = SqlBuilders.insertBuilder("user")
            .insertColumns("id", "name")
            .values(Arrays.asList(
                new User(10, "demo1"),
                new User(20, "demo2")
            )).build();
        String target = "INSERT INTO user(id, name) Values ( 10, 'demo1' ),( 20, 'demo2' )";
        Assert.assertEquals(target, insertSql);
    }

    /**
     * Entity 是继承体系
     */
    @Test
    public void insertRecursiveSqlTest() {
        String insertSql = SqlBuilders.insertBuilder("user")
            .insertColumns("id", "name", "age")
            .values(Arrays.asList(
                new UserExt(10, "demo1", 18),
                new UserExt(20, "demo2", 30)
            )).build();
        String target = "INSERT INTO user(id, name, age) Values ( 10, 'demo1', 18 ),( 20, 'demo2', 30 )";
        Assert.assertEquals(target, insertSql);
    }

    @Test
    public void deleteSqlTest() {
        String deleteSql = SqlBuilders.deleteBuilder("user")
            .where(Conditions.where()
                .equalsTo("id", 1)
            )
            .build();
        String target = "Delete From user WHERE id = 1 ";
        Assert.assertEquals(target, deleteSql);
    }

    @Test
    public void deleteSqlByStringTest() {
        String deleteSql = SqlBuilders.deleteBuilder("user")
            .where(Conditions.where()
                .equalsTo("name", "neko")
            )
            .build();
        String target = "Delete From user WHERE name = 'neko' ";
        Assert.assertEquals(target, deleteSql);
    }

    @Test
    public void updateSqlTest() {
        String updateSql = SqlBuilders.updateBuilder("user")
            .set(Conditions.set()
                .equalsTo("name", "neko")
            )
            .where(Conditions.where()
                .equalsTo("id", 1)
            )
            .build();
        String target = "Update user Set name = 'neko' WHERE id = 1 ";
        Assert.assertEquals(target, updateSql);
    }

    @Test
    public void selectSqlTest() {
        // Table <- columns, condition
        String selectSql = SqlBuilders.selectBuilder("user")
            .select("id")
            .where(Conditions.where()
                    .equalsTo("id", 1)
            ).orderBy(
                Conditions.orderBy()
                    .orderByAsc("a", "b")
            ).groupBy(
                Conditions.groupByWithHaving(Conditions.having().equalsTo("id", 1))
                    .groupBy("id", "name")
            ).limit(0, 10)
            .build();
        String target = "SELECT id FROM user WHERE id = 1 ORDER BY a ASC, b ASC GROUP BY id, name HAVING 1 = 1 and id = 1 LIMIT 0, 10 ";
        Assert.assertEquals(target, selectSql);
    }

    @Test
    public void selectSqlByPageTest() {
        // Table <- columns, condition
        String selectSql = SqlBuilders.selectBuilder("user")
            .select("id", "name")
            .where(Conditions.where()
                .like("id", 1)
            ).orderBy(
                Conditions.orderBy()
                    .orderByAsc("a", "b")
            ).groupBy(
                Conditions.groupByWithHaving(Conditions.having().equalsTo("id", 1))
                    .groupBy("id", "name")
            ).limitByPage(1, 10)
            .build();
        String target = "SELECT id, name FROM user WHERE id like 1 ORDER BY a ASC, b ASC GROUP BY id, name HAVING 1 = 1 and id = 1 LIMIT 10, 20 ";
        Assert.assertEquals(target, selectSql);
    }


}
