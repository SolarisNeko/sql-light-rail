package com.neko233.sql.lightrail.sqlString;

import com.neko233.sql.lightrail.SqlLightRail;
import com.neko233.sql.lightrail.condition.Conditions;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author SolarisNeko
 * Date on 2022-02-21
 **/
public class DeleteTest {

    @Test
    public void deleteSqlTest() {
        String deleteSql = SqlLightRail.deleteTable("user")
                .where(Conditions.where()
                        .equalsTo("id", 1)
                )
                .build();
        String target = "Delete From user WHERE id = 1";
        Assert.assertEquals(target, deleteSql);
    }

    @Test
    public void deleteSqlByStringTest() {
        String deleteSql = SqlLightRail.deleteTable("user")
                .where(Conditions.where()
                        .equalsTo("name", "neko")
                )
                .build();
        String target = "Delete From user WHERE name = 'neko'";
        Assert.assertEquals(target, deleteSql);
    }

}
