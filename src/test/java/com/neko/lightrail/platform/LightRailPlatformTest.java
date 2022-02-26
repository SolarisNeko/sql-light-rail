package com.neko.lightrail.platform;

import com.neko.lightrail.LightRailPlatform;
import com.neko.lightrail.LightRailPlatformFactory;
import com.neko.lightrail.SqlLightRail;
import com.neko.lightrail.builder.InsertSqlBuilder;
import com.neko.lightrail.builder.SqlBuilder;
import com.neko.lightrail.builder.UpdateSqlBuilder;
import com.neko.lightrail.condition.SetCondition;
import com.neko.lightrail.condition.WhereCondition;
import com.neko.lightrail.pojo.User;
import com.neko.lightrail.util.MyDataSource;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @title:
 * @description:
 * @author: SolarisNeko
 * @date: 2/26/2022
 */
public class LightRailPlatformTest {

    @Test
    public void baseTest_insert2User() throws Exception {
        LightRailPlatform lightRailPlatform = LightRailPlatformFactory.createLightRailPlatform(MyDataSource.getDataSource());

        InsertSqlBuilder builder = SqlLightRail.insertTable("user")
            .insertColumns("name")
            .values("('demo10'), ('demo11') ");
        Integer rowCount = lightRailPlatform.executeUpdate(builder);

        Assert.assertTrue(2 == rowCount);
    }

    @Test
    public void baseTest_insert2User_ORM() throws Exception {
        LightRailPlatform lightRailPlatform = LightRailPlatformFactory.createLightRailPlatform(MyDataSource.getDataSource());

        List<User> users = new ArrayList<User>() {{
            add(User.builder().name("demo21").build());
            add(User.builder().name("demo22").build());
        }};

        InsertSqlBuilder builder = SqlLightRail.insertTable("user")
            .insertColumns("name")
            .values(users);
        Integer rowCount = lightRailPlatform.executeUpdate(builder);

        Assert.assertTrue(2 == rowCount);
    }

    @Test
    public void baseTest_update2User() throws Exception {
        LightRailPlatform lightRailPlatform = LightRailPlatformFactory.createLightRailPlatform(MyDataSource.getDataSource());

        SqlBuilder where = SqlLightRail.updateTable("user")
            .set("create_time = '2022-01-01 11:11:11'")
            .where(WhereCondition.builder()
                .equalsTo("id", 1)
            );
        Integer rowCount = lightRailPlatform.executeUpdate(where);

        Assert.assertTrue(1 == rowCount);
    }

    @Test
    public void baseTest_update2User_SetDate() throws Exception {
        LightRailPlatform lightRailPlatform = LightRailPlatformFactory.createLightRailPlatform(MyDataSource.getDataSource());

        SqlBuilder where = SqlLightRail.updateTable("user")
            .set(SetCondition.builder().equalsTo("create_time", new Date()))
            .where(WhereCondition.builder()
                .equalsTo("id", 1)
            );
        Integer rowCount = lightRailPlatform.executeUpdate(where);

        Assert.assertTrue(1 == rowCount);
    }

}
