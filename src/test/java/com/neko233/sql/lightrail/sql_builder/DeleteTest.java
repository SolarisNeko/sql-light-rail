package com.neko233.sql.lightrail.sql_builder;

import com.neko233.sql.lightrail.SqlBuilder233;
import com.neko233.sql.lightrail.condition.single.Conditions;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author SolarisNeko
 * Date on 2022-02-21
 **/
public class DeleteTest {

    @Test
    public void deleteSqlTest() {
        String deleteSql = SqlBuilder233.deleteTable("user")
                .where(Conditions.where()
                        .equalsTo("id", 1)
                )
                .build();
        String target = "Delete From user WHERE id = 1";
        Assert.assertEquals(target, deleteSql);
    }

    @Test
    public void deleteSqlByStringTest() {
        String deleteSql = SqlBuilder233.deleteTable("user")
                .where(Conditions.where()
                        .equalsTo("name", "neko")
                )
                .build();
        String target = "Delete From user WHERE name = 'neko'";
        Assert.assertEquals(target, deleteSql);
    }

}
