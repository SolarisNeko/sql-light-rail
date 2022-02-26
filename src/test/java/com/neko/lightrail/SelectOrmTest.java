package com.neko.lightrail;

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
        LightRailPlatform lightRailPlatform = LightRailPlatformFactory.createLightRailPlatform(MyDataSource.getDataSource());

        lightRailPlatform.registerPlugin(new SlowSqlPlugin());

        List<UserWithEmail> dataList = lightRailPlatform.executeQuery(
            SqlLightRail.selectTable("user", UserWithEmail.class),
            UserWithEmail.class
        );
        System.out.println(dataList);
    }
}
