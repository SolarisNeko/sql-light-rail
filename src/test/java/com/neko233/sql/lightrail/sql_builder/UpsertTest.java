package com.neko233.sql.lightrail.sql_builder;

import com.neko233.sql.lightrail.SqlLightRail;
import com.neko233.sql.lightrail.condition.single.OnDuplicateUpdateCondition;
import com.neko233.sql.lightrail.pojo.User;
import com.neko233.sql.lightrail.pojo.UserExt;
import com.neko233.sql.lightrail.pojo.UserWithEmail;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * @author SolarisNeko
 * Date on 2022-02-21
 **/
public class UpsertTest {

    @Test
    public void upsertTest_onDuplicateKeySet_base() {
        String insertSql = SqlLightRail.insertTable("user")
                .columnNames("id", "name")
                .values(Arrays.asList(
                        new User(10, "demo1"),
                        new User(20, "demo2")
                ))
                .onDuplicateUpdate(OnDuplicateUpdateCondition.builder()
                        .equalsTo("name", "demo3")
                )
                .build();
        String target = "INSERT INTO user(id, name) Values (10, 'demo1'), (20, 'demo2') On Duplicate Key Update name = 'demo3'";
        Assert.assertEquals(target, insertSql);
    }

    @Test
    public void upsertTest_onDuplicateKeySet_values() {
        String insertSql = SqlLightRail.insertTable("user")
                .columnNames("id", "name")
                .values(Arrays.asList(
                        new User(10, "demo1"),
                        new User(20, "demo2")
                ))
                .onDuplicateUpdate(OnDuplicateUpdateCondition.builder()
                        .updateValue("name")
                )
                .build();
        String target = "INSERT INTO user(id, name) Values (10, 'demo1'), (20, 'demo2') On Duplicate Key Update `name` = values(`name`)";
        Assert.assertEquals(target, insertSql);
    }


}
