# SQL Light Rail

## 简介



### 介绍

SQL Light Rail (SQL 轻轨)

1. class SqlLightRail = SQL 链式编写
2. class ShardingKey = ShardingId 计算
3. class RepositoryManager = 多数据源管理

License 为 Apache-2.0

## Download

### Maven

```xml

<dependency>
    <groupId>com.neko233</groupId>
    <artifactId>sql-light-rail</artifactId>
    <version>1.1.0</version>
</dependency>

```

### Gradle

```groovy
implementation group: 'com.neko233', name: 'sql-light-rail', version: '1.1.0'
```

## 初衷 / 痛点

因 mybatis-plus/flux 等好用的 ORM 框架, 都依赖于 mybatis. 

但一旦离开了 mybatis 生态圈, 很多好用的机制不能拿出来独立使用. 

所以, 该项目做了以下的模块化:
1. 独立的 SQL stream 写法, 直接生成 sql 语句. 无需 mybatis-plus 重量级依赖.
2. 独立的 ORM 机制.
3. 独立的 Sharding DB 机制
4. Plugin Chain (插件化) 处理 SQL 执行过程.


# Use
Dependency
## maven
```xml
<!-- default use druid as DataSource -->
<dependency>
    <groupId>com.neko233</groupId>
    <artifactId>sql-light-rail</artifactId>
    <version>1.1.0</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.25</version>
</dependency>
```

## gradle
```kotlin
implementation("com.neko233:sql-light-rail:1.1.0")
implementation("mysql:mysql-connector-java:8.0.25")

```

# Code
## Sql Builder 链式构建 SQL 
```java

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
        String select = Sql233.selectTable("traffic_statistics_report")
                .select(selectCondition.build())
                .where(whereCondition.build())
                .groupBy(groupByCondition)
                .build();
        // 4 execute
        System.out.println(select);
        String target = "SELECT app_id, channel_id, sum(pay_money) as 'sum_money', register_time, pay_time, package_id FROM traffic_statistics_report  WHERE app_id = ? and channel_id = '-1' and package_id = '-1' and register_time <= ? and register_time <= ? GROUP BY register_time, pay_time, channel_id, package_id";
        Assert.assertEquals(target, select);
    }

    // ...
}
```


## ORM
### Select
定义对象
```java
/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Column("id")
    private int id;

    @Column("name")
    private String name;

}
```

查询部分
```java

public class SelectDbTest {

    Db db = new Db(MyDataSource.getDefaultDataSource());

    public SelectDbTest() throws Exception {
    }


    @Test
    public void selectTest_IntegerOrm() throws Exception {

        List<Integer> testIntValue = db
                .executeQuery("Select 1 From dual ", Integer.class);

        Assert.assertEquals(1, testIntValue.get(0).intValue());
    }

    @Test
    public void selectTest_ObjectOrm() throws Exception {

        List<User> users = db.executeQuery("Select id, name From user Limit 0, 1 ", User.class);

        Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void selectTest_paramsSet() throws Exception {
        Object[] params = {1};
        List<User> users = db
                .executeQuery("Select id, name From user Where id != ? Limit 0, 1 ", params, User.class);

        Assert.assertTrue(users.size() > 0);
    }

}
```