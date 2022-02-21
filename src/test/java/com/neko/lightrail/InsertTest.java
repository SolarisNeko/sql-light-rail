package com.neko.lightrail;

import com.neko.lightrail.pojo.User;
import com.neko.lightrail.pojo.UserExt;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author SolarisNeko
 * @date 2022-02-21
 **/
public class InsertTest {

    /**
     * Entity 是单个对象
     */
    @Test
    public void insertSqlTest() {
        String insertSql = SqlLightRail.insertBuilder("user")
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
        String insertSql = SqlLightRail.insertBuilder("user")
                .insertColumns("id", "name", "age")
                .values(Arrays.asList(
                        new UserExt(10, "demo1", 18),
                        new UserExt(20, "demo2", 30)
                )).build();
        String target = "INSERT INTO user(id, name, age) Values ( 10, 'demo1', 18 ),( 20, 'demo2', 30 )";
        Assert.assertEquals(target, insertSql);
    }

}
