package com.neko233.lightrail.orm;

import com.neko233.lightrail.RailPlatform;
import com.neko233.lightrail.RailPlatformFactory;
import com.neko233.lightrail.SqlLightRail;
import com.neko233.lightrail.plugin.SlowSqlPlugin;
import com.neko233.lightrail.pojo.UserLackFields;
import com.neko233.lightrail.pojo.UserWithEmail;
import com.neko233.lightrail.dataSource.MyDataSource;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author SolarisNeko
 * Date on 2022-02-26
 */
public class SelectOrmTest {

    RailPlatform railPlatform = RailPlatformFactory.createLightRailPlatform(MyDataSource.getDefaultDataSource());

    public SelectOrmTest() throws Exception {
    }

    @Test
    public void selectOrmTest() throws Exception {

        railPlatform.addGlobalPlugin(new SlowSqlPlugin());

        List<UserWithEmail> dataList = railPlatform.executeQuery(
            SqlLightRail.selectTable("user", UserWithEmail.class),
            UserWithEmail.class
        );
        for (UserWithEmail user : dataList) {
            Assert.assertTrue(user.getName() != null);
        }
    }

    @Test
    public void selectOrmTest_lackSomeField_1() throws Exception {

        railPlatform.addGlobalPlugin(new SlowSqlPlugin());

        List<UserLackFields> dataList = railPlatform.executeQuery(
            SqlLightRail.selectTable("user", UserLackFields.class),
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

        railPlatform.addGlobalPlugin(new SlowSqlPlugin());

        List<UserLackFields> dataList = railPlatform.executeQuery(
            SqlLightRail.selectTable("user").select("id", "name"),
            UserLackFields.class
        );

        for (UserLackFields userLackFields : dataList) {
            Assert.assertEquals(null, userLackFields.getCreateTime());
        }
    }


}
