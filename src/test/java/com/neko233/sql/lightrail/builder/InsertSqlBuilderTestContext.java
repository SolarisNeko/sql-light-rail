package com.neko233.sql.lightrail.builder;

import com.neko233.sql.lightrail.SqlLightRail;
import com.neko233.sql.lightrail.condition.OnDuplicateUpdateCondition;
import org.junit.Assert;
import org.junit.Test;

public class InsertSqlBuilderTestContext {

    @Test
    public void onDuplicateUpdateTest() {
        String sql = SqlLightRail.insertTable("user")
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