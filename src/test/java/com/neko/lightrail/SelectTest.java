package com.neko.lightrail;

import com.neko.lightrail.builder.SelectSqlBuilder;
import com.neko.lightrail.condition.Conditions;
import com.neko.lightrail.condition.GroupByCondition;
import com.neko.lightrail.condition.JoinCondition;
import com.neko.lightrail.condition.OnCondition;
import com.neko.lightrail.condition.SelectCondition;
import com.neko.lightrail.condition.WhereCondition;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static com.neko.lightrail.condition.Condition.PLACEHOLDER;

/**
 * @author SolarisNeko
 * @date 2022-02-21
 **/
public class SelectTest {

    /**
     * 模拟复杂业务场景，动态维度 SQL
     */
    @Test
    public void businessSelectDynamicDimension() {
        boolean isGroupByPackageId = true;
        // 1 Base SQL
        SelectCondition selectCondition = SelectCondition.builder()
                .column("app_id")
                .column("channel_id")
                .column("sum(pay_money)", "sum_money")
                .column("register_time")
                .column("pay_time");
        WhereCondition whereCondition = WhereCondition.builder()
                .equalsTo("app_id", PLACEHOLDER)
                .equalsTo("channel_id", "-1")
                .equalsTo("package_id", "-1")
                .lessThanOrEquals("register_time", PLACEHOLDER)
                .lessThanOrEquals("register_time", PLACEHOLDER);
        GroupByCondition groupByCondition = GroupByCondition.builder()
                .groupBy("register_time", "pay_time", "channel_id");
        // 2 [Business Rule] dynamic sql add by batch
        if (isGroupByPackageId) {
            selectCondition.column("package_id");
            groupByCondition.groupBy("package_id");
        }
        // 3 build SQL
        String select = SqlLightRail.selectTable("new_traffic_ltv_sum_daily")
                .select(selectCondition.build())
                .where(whereCondition.build())
                .groupBy(groupByCondition)
                .build();
        // 4 execute
        System.out.println(select);
        String target = "SELECT app_id, channel_id, sum(pay_money) as 'sum_money', register_time, pay_time, package_id FROM new_traffic_ltv_sum_daily  WHERE app_id = ? and channel_id = '-1' and package_id = '-1' and register_time <= ? and register_time <= ? GROUP BY register_time, pay_time, channel_id, package_id";
        Assert.assertEquals(target, select);
    }

    @Test
    public void generatePlaceHolder() {
        SelectSqlBuilder innerBuilder = SqlLightRail.selectTable("inner_demo")
                .select("id", "name")
                .where(WhereCondition.builder()
                        .equalsTo("id", PLACEHOLDER)
                );
        String select = SqlLightRail.selectSubTable(innerBuilder)
                .select("id", "name")
                .build();
        String target = "SELECT id, name FROM  ( SELECT id, name FROM inner_demo WHERE id = ? )  ";
        Assert.assertEquals(target, select);
    }

    @Test
    public void innerSelectSqlTest() {
        SelectSqlBuilder innerBuilder = SqlLightRail.selectTable("inner_demo")
                .select("id", "name")
                .where(WhereCondition.builder()
                        .equalsTo("id", 1)
                );
        String select = SqlLightRail.selectSubTable(innerBuilder)
                .select("id", "name")
                .build();
        String target = "SELECT id, name FROM  ( SELECT id, name FROM inner_demo WHERE id = 1 )  ";
        Assert.assertEquals(target, select);
    }

    @Test
    public void selectSqlTest() {
        // Table <- columns, condition
        String selectSql = SqlLightRail.selectTable("user")
                .select("id")
                .where(Conditions.where()
                        .equalsTo("id", 1)
                ).orderBy(
                        Conditions.orderBy()
                                .orderByAsc("a", "b")
                ).groupBy(
                        Conditions.groupByWithHaving(Conditions.having().equalsTo("id", 1))
                                .groupBy("id", "name")
                ).limit(0, 10)
                .build();
        String target = "SELECT id FROM user WHERE id = 1 ORDER BY a ASC, b ASC GROUP BY id, name HAVING 1 = 1 and id = 1 LIMIT 0, 10 ";
        Assert.assertEquals(target, selectSql);
    }

    @Test
    public void selectSqlTest_limitByPage() {
        // Table <- columns, condition
        String selectSql = SqlLightRail.selectTable("user")
                .select("id", "name")
                .where(Conditions.where()
                        .like("id", 1)
                ).orderBy(
                        Conditions.orderBy()
                                .orderByAsc("a", "b")
                ).groupBy(
                        Conditions.groupByWithHaving(Conditions.having().equalsTo("id", 1))
                                .groupBy("id", "name")
                ).limitByPage(1, 10)
                .build();
        String target = "SELECT id, name FROM user WHERE id like 1 ORDER BY a ASC, b ASC GROUP BY id, name HAVING 1 = 1 and id = 1 LIMIT 10, 20 ";
        Assert.assertEquals(target, selectSql);
    }

    @Test
    public void selectSqlTest_multiTables() {
        // Table <- columns, condition
        String selectSql = SqlLightRail.selectTable("user a", "user_role b")
                .select("b.id", "a.name")
                .where(Conditions.where()
                        .equalsTo("b.id", 1)
                )
                .build();
        String target = "SELECT b.id, a.name FROM user a,user_role b WHERE b.id = 1";
        Assert.assertEquals(target, selectSql);
    }


    @Test
    public void selectSqlTest_joinOn_base() {
        // Table <- columns, condition
        String selectSql = SqlLightRail.selectTable("user a")
                .select("a.id", "a.name")
                .join(JoinCondition.builder()
                        .join("user_role b")
                        .on("a.id = b.id")
                )
                .build();
        String target = "SELECT a.id, a.name FROM user a JOIN user_role b ON a.id = b.id ";
        Assert.assertEquals(target, selectSql);
    }

    @Test
    public void selectSqlTest_joinOn_advanced_1() {
        // Table <- columns, condition
        String selectSql = SqlLightRail.selectTable("user a")
                .select("a.id", "a.name")
                .alias(new HashMap<String, String>() {{
                    put("a.id", "编号");
                }})
                .join(JoinCondition.builder()
                        .join("user_role b")
                        .on("a.id = b.id")
                        .on("1=1")
                        .on(OnCondition.builder()
                                .equalsTo("a", "id", 1)
                                .equalsTo("a", "name", "neko")
                        )
                )
                .build();
        String target = "SELECT a.id as '编号', a.name FROM user a JOIN user_role b ON a.id = b.id and 1=1 and a.id = 1 and a.name = 'neko' ";
        Assert.assertEquals(target, selectSql);
    }

}
