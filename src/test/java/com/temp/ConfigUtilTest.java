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
        System.out.println("enable: " + configUtil.getBoolean("lightrail.slowSQL.enable"));
        System.out.println("timeout: " + configUtil.getInteger("lightrail.slowSQL.timeout"));
        System.out.println("log-prefix: " + configUtil.getString("lightrail.slowSQL.log-prefix"));
    }

    @Test
    public void getAllValueTest() {
        configUtil.getAllValue().forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
