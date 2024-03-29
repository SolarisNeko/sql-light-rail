package com.neko233.sql.lightrail.sql_builder;

import com.neko233.sql.lightrail.SqlBuilder233;
import com.neko233.sql.lightrail.pojo.User;
import com.neko233.sql.lightrail.pojo.UserExt;
import com.neko233.sql.lightrail.pojo.UserWithEmail;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * @author SolarisNeko
 * Date on 2022-02-21
 **/
public class InsertTest {

    /**
     * Entity 是单个对象
     */
    @Test
    public void insertSqlTest() {
        String insertSql = SqlBuilder233.insertTable("user")
                .columnNames("id", "name")
                .ormForInsertValues(Arrays.asList(
                        new User(10, "demo1"),
                        new User(20, "demo2")
                )).build();
        String target = "INSERT INTO user(id, name) Values (10, 'demo1'), (20, 'demo2')";
        Assert.assertEquals(target, insertSql);
    }

    /**
     * Entity 是继承体系
     */
    @Test
    public void insertRecursiveSqlTest() {
        String insertSql = SqlBuilder233.insertTable("user")
                .ormForInsertValues(Arrays.asList(
                        new UserExt(10, "demo1", 18),
                        new UserExt(20, "demo2", 30)
                )).build();
        String target = "INSERT INTO user(age, id, name) Values (18, 10, 'demo1'), (30, 20, 'demo2')";
        Assert.assertEquals(target, insertSql);
    }


    /**
     * Insert Date object
     */
    @Test
    public void insertTest_JavaDate() {
        String insertSql = SqlBuilder233.insertTable("user")
            .ormForInsertValues(
                new ArrayList<UserWithEmail>() {{
                    Date createTime = new Date();
                    add(UserWithEmail.builder().name("test_date_3").createTime(createTime).build());
                    add(UserWithEmail.builder().name("test_date_2").createTime(createTime).build());
                }}
            ).build();
        String demo = "INSERT INTO user(create_time, email, id, name) Values ('2022-02-26 10:38:39', null, null, 'test_date_3'), ('2022-02-26 10:38:39', null, null, 'test_date_2')";
        System.out.println(insertSql);
    }

    /**
     * Insert Date object
     */
    @Test
    public void insertTest_placeHolderTemplate_1() {
        String insertSql = SqlBuilder233.generateInsertTemplateAuto(User.class, 1L).build();
        String target = "INSERT INTO user(id, name) Values (?,?)";
        Assert.assertEquals(target, insertSql);
    }

    @Test
    public void insertTest_placeHolderTemplate_4() {
        String insertSql = SqlBuilder233.generateInsertTemplateAuto("user", UserExt.class, 4L).build();
        String target = "INSERT INTO user(age, id, name) Values (?,?,?), (?,?,?), (?,?,?), (?,?,?)";
        Assert.assertEquals(target, insertSql);
    }

}
