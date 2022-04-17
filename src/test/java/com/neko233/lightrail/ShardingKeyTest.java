package com.neko233.lightrail;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author SolarisNeko
 * Date on 2022-04-17
 */
public class ShardingKeyTest {

    @Test
    public void baseTest() throws Exception {
        Integer result = ShardingKey.calculateShardingKeySuffix(104_8577);
        Assert.assertEquals(Integer.valueOf(1), result);
    }

    @Test
    public void testCalculateShardingKeySuffix() throws Exception {
        Integer result = ShardingKey.calculateShardingKeySuffix(104_8578);
        Assert.assertEquals(Integer.valueOf(2), result);
    }

    @Test
    public void getShardingStep() throws Exception {
        Integer result = ShardingKey.getShardingStep();
        Assert.assertEquals(Integer.valueOf(104_8576), result);
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme