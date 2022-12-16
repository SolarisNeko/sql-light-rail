package com.neko233.sql.lightrail.sharding;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author SolarisNeko
 * Date on 2022-12-16
 */
public class ShardingId100WTest {


    @Test
    public void baseTest() throws Exception {
        String result = ShardingKey.getShardingKeyName("user_", 1);
        Assert.assertEquals("user_0", result);
    }

    @Test
    public void testCalculateShardingKeySuffix() throws Exception {
        String result = ShardingKey.getShardingKeyName("user_", 100_0000 + 1);
        Assert.assertEquals("user_1", result);
    }


}