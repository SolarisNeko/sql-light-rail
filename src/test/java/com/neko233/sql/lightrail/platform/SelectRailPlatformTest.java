package com.neko233.sql.lightrail.platform;

import com.neko233.sql.lightrail.RailPlatform;
import com.neko233.sql.lightrail.RailPlatformFactory;
import com.neko233.sql.lightrail.dataSource.MyDataSource;
import com.neko233.sql.lightrail.pojo.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author SolarisNeko
 * @date 4/23/2022
 */
public class SelectRailPlatformTest {


    @Test
    public void selectTest_IntegerOrm() throws Exception {
        RailPlatform railPlatform = RailPlatformFactory.createLightRailPlatform(MyDataSource.getDefaultDataSource());

        List<Integer> testIntValue = railPlatform.executeQuery("Select 1 From dual ", Integer.class);

        Assert.assertEquals(1, testIntValue.get(0).intValue());
    }

    @Test
    public void selectTest_ObjectOrm() throws Exception {
        RailPlatform railPlatform = RailPlatformFactory.createLightRailPlatform(MyDataSource.getDefaultDataSource());

        List<User> users = railPlatform.executeQuery("Select id, name From user Limit 0, 1 ", User.class);

        Assert.assertTrue(users != null);
    }

}
