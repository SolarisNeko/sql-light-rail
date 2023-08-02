package com.neko233.sql.lightrail.sql_builder;

import com.neko233.sql.lightrail.SqlBuilder233;
import com.neko233.sql.lightrail.condition.single.OnDuplicateUpdateCondition;
import org.junit.Assert;
import org.junit.Test;

public class InsertSqlBuilderTestContext233 {

    @Test
    public void onDuplicateUpdateTest() {
        String sql = SqlBuilder233.insertTable("user")
                .columnNames("id", "name", "age")
                .singleRowValue(1, "neko", 18)
                .onDuplicateUpdate(OnDuplicateUpdateCondition.builder()
                        .equalsTo("name", "nekoV2")
                )
                .build();

        Assert.assertEquals(
                "INSERT INTO user(id, name, age) Values (1,'neko',18) On Duplicate Key Update name = 'nekoV2'",
                sql
        );
    }

}