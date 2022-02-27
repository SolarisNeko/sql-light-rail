package com.neko.lightrail.orm;

import com.neko.lightrail.RailPlatform;
import com.neko.lightrail.RailPlatformFactory;
import com.neko.lightrail.SqlLightRail;
import com.neko.lightrail.plugin.SlowSqlPlugin;
import com.neko.lightrail.pojo.UserWithEmail;
import com.neko.lightrail.util.MyDataSource;
import org.junit.Test;

import java.util.List;

/**
 * @author SolarisNeko
 * @date 2022-02-26
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
