package com.neko233.sql.lightrail.platform;

import com.neko233.sql.lightrail.RailPlatform;
import com.neko233.sql.lightrail.RailPlatformFactory;
import com.neko233.sql.lightrail.SqlLightRail;
import com.neko233.sql.lightrail.builder.InsertSqlBuilder;
import com.neko233.sql.lightrail.dataSource.MyDataSource;
import com.neko233.sql.lightrail.pojo.User;
import com.neko233.sql.lightrail.pojo.UserWithEmail;
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
public class InsertRailPlatformTest {

    RailPlatform railPlatform = RailPlatformFactory.createLightRailPlatform(MyDataSource.getDefaultDataSource());

    public InsertRailPlatformTest() throws Exception {
    }


    @Test
    public void baseTest_insert2User() throws Exception {

        InsertSqlBuilder builder = SqlLightRail.insertTable("user")
            .columnNames("name")
            .values("('demo10'), ('demo11') ");
        Integer rowCount = railPlatform.executeUpdate(builder);

        Assert.assertTrue(2 == rowCount);
    }

    @Test
    public void baseTest_insert2User_single_ORM() throws Exception {
        RailPlatform railPlatform = RailPlatformFactory.createLightRailPlatform(MyDataSource.getDefaultDataSource());

        List<User> valueList = new ArrayList<User>() {{
            add(User.builder().name("demo21").build());
            add(User.builder().name("demo22").build());
        }};

        InsertSqlBuilder builder = SqlLightRail.insertTable("user")
            .columnNames("name")
            .values(valueList);
        Integer rowCount = railPlatform.executeUpdate(builder);

        Assert.assertTrue(2 == rowCount);
    }

    @Test
    public void baseTest_insert2User_multi_ORM() throws Exception {
        RailPlatform railPlatform = RailPlatformFactory.createLightRailPlatform(MyDataSource.getDefaultDataSource());

        List<UserWithEmail> valueList = new ArrayList<UserWithEmail>() {{
            add(UserWithEmail.builder().name("demo21").email("123@qq.com").build());
            add(UserWithEmail.builder().name("demo22").email("456@qq.com").build());
        }};

        InsertSqlBuilder builder = SqlLightRail.insertTable("user")
            .values(valueList);
        Integer rowCount = railPlatform.executeUpdate(builder);

        Assert.assertTrue(2 == rowCount);
    }

    @Test
    public void generate_insert_template() {
        String build = SqlLightRail.generateInsertTemplate(User.class, 1L).build();
        String target = "INSERT INTO user(id, name) Values (?,?)";
        Assert.assertEquals(target, build);
    }


}
