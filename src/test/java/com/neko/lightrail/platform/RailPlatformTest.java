package com.neko.lightrail.platform;

import com.alibaba.druid.sql.builder.SQLBuilder;
import com.neko.lightrail.RailPlatform;
import com.neko.lightrail.RailPlatformFactory;
import com.neko.lightrail.SqlLightRail;
import com.neko.lightrail.builder.InsertSqlBuilder;
import com.neko.lightrail.builder.SelectSqlBuilder;
import com.neko.lightrail.builder.SqlBuilder;
import com.neko.lightrail.condition.SetCondition;
import com.neko.lightrail.condition.WhereCondition;
import com.neko.lightrail.pojo.User;
import com.neko.lightrail.pojo.UserExt;
import com.neko.lightrail.util.MyDataSource;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @title:
 * @description:
 * @author: SolarisNeko
 * @date: 2/26/2022
 */
public class RailPlatformTest {

    RailPlatform railPlatform = RailPlatformFactory.createLightRailPlatform(MyDataSource.getDataSource());

    public RailPlatformTest() throws Exception {
    }


    @Test
    public void baseTest_selectByAutoGenerate() {
        List<User> users = railPlatform.executeQuery(User.class);
        System.out.println(users);
    }

    @Test
    public void baseTest_insert2User() throws Exception {

        InsertSqlBuilder builder = SqlLightRail.insertTable("user")
            .insertColumns("name")
            .values("('demo10'), ('demo11') ");
        Integer rowCount = railPlatform.executeUpdate(builder);

        Assert.assertTrue(2 == rowCount);
    }

    @Test
    public void baseTest_insert2User_ORM() throws Exception {
        RailPlatform railPlatform = RailPlatformFactory.createLightRailPlatform(MyDataSource.getDataSource());

        List<User> valueList = new ArrayList<User>() {{
            add(User.builder().name("demo21").build());
            add(User.builder().name("demo22").build());
        }};

        InsertSqlBuilder builder = SqlLightRail.insertTable("user")
            .insertColumns("name")
            .values(valueList);
        Integer rowCount = railPlatform.executeUpdate(builder);

        Assert.assertTrue(2 == rowCount);
    }

    @Test
    public void baseTest_update2User() throws Exception {
        RailPlatform railPlatform = RailPlatformFactory.createLightRailPlatform(MyDataSource.getDataSource());

        SqlBuilder updateSql = SqlLightRail.updateTable("user")
            .set("create_time = '2022-01-01 11:11:11'")
            .where(WhereCondition.builder()
                .equalsTo("id", 1)
            );
        Integer rowCount = railPlatform.executeUpdate(updateSql);

        Assert.assertTrue(1 == rowCount);
    }

    @Test
    public void baseTest_update2User_SetDate() throws Exception {
        RailPlatform railPlatform = RailPlatformFactory.createLightRailPlatform(MyDataSource.getDataSource());

        SqlBuilder where = SqlLightRail.updateTable("user")
            .set(SetCondition.builder().equalsTo("create_time", new Date()))
            .where(WhereCondition.builder()
                .equalsTo("id", 1)
            );
        Integer rowCount = railPlatform.executeUpdate(where);

        Assert.assertTrue(1 == rowCount);
    }

}
