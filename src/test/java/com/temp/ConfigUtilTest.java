package com.temp;

import com.neko.lightrail.util.ConfigUtil;
import org.junit.Before;
import org.junit.Test;

public class ConfigUtilTest {

    private ConfigUtil configUtil;

    @Before
    public void init() {
        configUtil = new ConfigUtil();
    }

    @Test
    public void basicTest() {
        System.out.println("lightrail.slowSQL.enable: " + configUtil.getBoolean("lightrail.slowSQL.enable"));
        System.out.println("lightrail.connection.timeout: " + configUtil.getInteger("lightrail.connection.timeout"));
        System.out.println("lightrail.connection.driver.name: " + configUtil.getString("lightrail.connection.driver.name"));
        System.out.println("lightrail.connection.driver.version: " + configUtil.getDouble("lightrail.connection.driver.version"));
        System.out.println("lightrail.plugins: " + configUtil.getStringList("lightrail.plugins"));
    }

    @Test
    public void getAllValueTest() {
        configUtil.getAllValue().forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
