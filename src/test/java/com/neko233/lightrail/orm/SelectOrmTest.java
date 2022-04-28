package com.neko233.lightrail.orm;

import com.neko233.lightrail.RailPlatform;
import com.neko233.lightrail.RailPlatformFactory;
import com.neko233.lightrail.SqlLightRail;
import com.neko233.lightrail.plugin.SlowSqlPlugin;
import com.neko233.lightrail.pojo.UserLackFields;
import com.neko233.lightrail.pojo.UserWithEmail;
import com.neko233.lightrail.dataSource.MyDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author SolarisNeko
 * Date on 2022-02-26
 */
public class SelectOrmTest {

    RailPlatform railPlatform;

    @Before
    public void before() throws Exception {
        railPlatform = RailPlatformFactory.createLightRailPlatform(MyDataSource.getDefaultDataSource());
        railPlatform.removeAllPlugins();
        railPlatform.addGlobalPlugin(new SlowSqlPlugin());
    }

    @Test
    public void selectOrmTest_original_SQL_String() throws Exception {

        List<UserWithEmail> dataList = railPlatform.executeQuery(
            "select id, name From user Limit 0, 1",
            UserWithEmail.class
        );
        for (UserWithEmail user : dataList) {
            Assert.assertTrue(user.getName() != null);
        }
    }

    @Test
    public void selectOrmTest_original_SQL_With_ShardingKey() throws Exception {

        // 可分库
        List<UserWithEmail> dataList = railPlatform.executeQuery(
            "select name From user Limit 0, 1",
            UserWithEmail.class
        );
        for (UserWithEmail user : dataList) {
            Assert.assertTrue(user.getId() == null);
        }
    }

    @Test
    public void selectOrmTest() throws Exception {

        List<UserWithEmail> dataList = railPlatform.executeQuery(
            SqlLightRail.selectTable("user", UserWithEmail.class)
                    .limitByPage(1, 5),
            UserWithEmail.class
        );
        for (UserWithEmail user : dataList) {
            Assert.assertTrue(user.getName() != null);
        }
    }

    @Test
    public void selectOrmTest_lackSomeField_1() throws Exception {

        List<UserLackFields> dataList = railPlatform.executeQuery(
            SqlLightRail.selectTable("user", UserLackFields.class)
                    .limitByPage(1, 10),
            UserLackFields.class
        );
        for (UserLackFields userLackFields : dataList) {
            Assert.assertTrue(userLackFields.getName() != null);
        }
    }

    /**
     * UserLackFields: name, create_time
     * Select SQL: id, name
     * Target:
     *
     * @throws Exception
     */
    @Test
    public void selectOrmTest_lackSomeField_2() throws Exception {

        List<UserLackFields> dataList = railPlatform.executeQuery(
            SqlLightRail.selectTable("user").select("id", "name")
                    .limit(0, 5),
            UserLackFields.class
        );

        for (UserLackFields userLackFields : dataList) {
            Assert.assertEquals(null, userLackFields.getCreateTime());
        }
    }


}
