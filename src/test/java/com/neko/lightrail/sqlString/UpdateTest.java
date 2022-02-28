package com.neko.lightrail.sqlString;

import com.neko.lightrail.SqlLightRail;
import com.neko.lightrail.condition.Conditions;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author SolarisNeko
 * Date on 2022-02-21
 **/
public class UpdateTest {

    @Test
    public void updateSqlTest() {
        String updateSql = SqlLightRail.updateTable("user")
                .set(Conditions.set()
                        .equalsTo("name", "neko")
                )
                .where(Conditions.where()
                        .equalsTo("id", 1)
                )
                .build();
        String target = "Update user Set name = 'neko' WHERE id = 1 ";
        Assert.assertEquals(target, updateSql);
    }

}
