package com.neko233.lightrail.orm;

import com.neko233.lightrail.RailPlatform;
import com.neko233.lightrail.RailPlatformFactory;
import com.neko233.lightrail.SqlLightRail;
import com.neko233.lightrail.plugin.SlowSqlPlugin;
import com.neko233.lightrail.pojo.UserWithEmail;
import com.neko233.lightrail.dataSource.MyDataSource;
import org.junit.Test;

import java.util.List;

/**
 * @author SolarisNeko
 * Date on 2022-02-26
 */
public class SelectOrmTest {

    @Test
    public void selectOrmTest() throws Exception {
        RailPlatform railPlatform = RailPlatformFactory.createLightRailPlatform(MyDataSource.getDataSource());

        railPlatform.addGlobalPlugin(new SlowSqlPlugin());

        List<UserWithEmail> dataList = railPlatform.executeQuery(
            SqlLightRail.selectTable("user", UserWithEmail.class),
            UserWithEmail.class
        );
        System.out.println(dataList);
    }
}
