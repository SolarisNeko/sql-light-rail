package com.neko233.sql.lightrail.sql_builder;

import com.neko233.sql.lightrail.SqlBuilder233;
import com.neko233.sql.lightrail.condition.single.Conditions;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author SolarisNeko
 * Date on 2022-02-21
 **/
public class UpdateTest {

    @Test
    public void updateSqlTest() {
        String updateSql = SqlBuilder233.updateTable("user")
                .set(Conditions.set()
                        .equalsTo("name", "neko")
                )
                .where(Conditions.where()
                        .equalsTo("id", 1)
                )
                .build();
        String target = "Update user Set name = 'neko' WHERE id = 1";
        Assert.assertEquals(target, updateSql);
    }

}
