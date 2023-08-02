package com.neko233.sql.lightrail.db;

import com.neko233.sql.lightrail.SqlBuilder233;
import com.neko233.sql.lightrail.datasource.MyDataSource;
import com.neko233.sql.lightrail.pojo.User;
import com.neko233.sql.lightrail.pojo.UserWithEmail;
import com.neko233.sql.lightrail.sql_builder.InsertSqlBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @title:
 * @description:
 * @author: SolarisNeko
 * Date on: 2/26/2022
 */
public class InsertDbTest {

    Db db = new Db(MyDataSource.getDefaultDataSource());

    public InsertDbTest() throws Exception {
    }


    @Test
    public void generate_insert_template() {
        String build = SqlBuilder233.generateInsertTemplateAuto(User.class, 1L).build();
        String target = "INSERT INTO user(id, name) Values (?,?)";
        Assert.assertEquals(target, build);
    }

    @Test
    public void baseTest_insert2User() throws Exception {

        InsertSqlBuilder builder = SqlBuilder233.insertTable("user")
                .columnNames("name")
                .values("('demo10'), ('demo11') ");
        Integer rowCount = db
                .executeUpdate(builder.build());

        Assert.assertTrue(2 == rowCount);
    }

    @Test
    public void baseTest_insert2User_single_ORM() throws Exception {

        List<User> valueList = new ArrayList<User>() {{
            add(User.builder().name("demo21").build());
            add(User.builder().name("demo22").build());
        }};

        InsertSqlBuilder builder = SqlBuilder233.insertTable("user")
                .columnNames("name")
                .ormForInsertValues(valueList);
        Integer rowCount = db
                .executeUpdate(builder.build());

        Assert.assertTrue(2 == rowCount);
    }

    @Test
    public void baseTest_insert2User_multi_ORM() throws Exception {

        List<UserWithEmail> valueList = new ArrayList<UserWithEmail>() {{
            add(UserWithEmail.builder().name("demo21").email("123@qq.com").build());
            add(UserWithEmail.builder().name("demo22").email("456@qq.com").build());
        }};

        InsertSqlBuilder builder = SqlBuilder233.insertTable("user")
                .ormForInsertValues(valueList);
        Integer rowCount = db
                .executeUpdate(builder.build());

        Assert.assertTrue(2 == rowCount);
    }



}
