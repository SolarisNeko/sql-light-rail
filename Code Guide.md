# Code Guide For `SQL Light Rail`
## Dependency
maven
```xml
<!-- ORM -->
<dependency>
    <groupId>com.neko233</groupId>
    <artifactId>sql-light-rail</artifactId>
    <version>0.1.1</version>
</dependency>
<!-- DataSource -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.8</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.25</version>
</dependency>
```

# 组装 SQL 入口 - class SqlLightRail
动态组装 SQL


# DAO 统一操作层 - class RailPlatform 
## 多数据源演示
需要 2 个依赖, DataSource + Connector.
```xml

```
## Demo Code
```java
    DataSource ds = DruidDataSourceFactory.createDataSource(getDefaultDbConfig());
    DataSource ds1 = DruidDataSourceFactory.createDataSource(getMultiDataSource_1());

    public static Properties getDefaultDbConfig() {
        Properties properties = new Properties();
        properties.put(PROP_URL, "jdbc:mysql://localhost:3306/sql_light_rail");
        properties.put(PROP_USERNAME, "root");
        properties.put(PROP_PASSWORD, "root");
        properties.put(PROP_INITIALSIZE, "5");
        properties.put(PROP_MINIDLE, "5");
        properties.put(PROP_MAXACTIVE, "10");
        properties.put(PROP_MAXWAIT, "10000");
        return properties;
    }
    
    public static Properties getMultiDataSource_1() {
        Properties properties = new Properties();
        properties.put(PROP_URL, "jdbc:mysql://localhost:3306/sql_light_rail_1");
        properties.put(PROP_USERNAME, "root");
        properties.put(PROP_PASSWORD, "root");
        properties.put(PROP_INITIALSIZE, "5");
        properties.put(PROP_MINIDLE, "5");
        properties.put(PROP_MAXACTIVE, "10");
        properties.put(PROP_MAXWAIT, "10000");
        return properties;
    }

    public MultiDataSourceTest() throws Exception {
    }

    @Test
    public void multiDataSourceTest() throws Exception {
        // 多个 dataSource
        RailPlatform platform = RailPlatformFactory.createLightRailPlatform(ds);
        platform.addDataSource("ds1", ds1);

        System.out.println("--------- sql_light_rail 数据源 -------------- ");
        List<User> users = platform.executeQuery(SqlStatement.builder()
            .sql(SqlLightRail.selectTable(User.class)
                .limitByPage(1, 1)
                .build())
            .returnType(User.class)
            .build());
        users.forEach(System.out::println);

        System.out.println("--------- sql_light_rail_1 数据源 -------------- ");
        List<User> otherUsers = platform.executeQuery(SqlStatement.builder()
            .shardingKey("ds1")
            .sql(SqlLightRail.selectTable(User.class)
                .limit(0, 1)
                .build())
            .returnType(User.class)
            .build());
        otherUsers.forEach(System.out::println);
    }
```