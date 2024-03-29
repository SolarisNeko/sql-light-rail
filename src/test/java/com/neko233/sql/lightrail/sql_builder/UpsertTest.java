package com.neko233.sql.lightrail.sql_builder;

import com.neko233.sql.lightrail.SqlBuilder233;
import com.neko233.sql.lightrail.condition.single.OnDuplicateUpdateCondition;
import com.neko233.sql.lightrail.pojo.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author SolarisNeko
 * Date on 2022-02-21
 **/
public class UpsertTest {

    @Test
    public void upsertTest_onDuplicateKeySet_base() {
        String insertSql = SqlBuilder233.insertTable("user")
                .columnNames("id", "name")
                .ormForInsertValues(Arrays.asList(
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
        String insertSql = SqlBuilder233.insertTable("user")
                .columnNames("id", "name")
                .ormForInsertValues(Arrays.asList(
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
