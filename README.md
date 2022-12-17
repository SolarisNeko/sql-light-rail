# SQL Light Rail

## 简介



### 介绍

SQL Light Rail (SQL 轻轨)

1. class SqlLightRail
2. class ShardingKey = sharding 计算
3. class RepositoryManager. shardingDB 管理.

License 为 Apache2.0

## Download

### Maven

```xml

<dependency>
    <groupId>com.neko233</groupId>
    <artifactId>sql-light-rail</artifactId>
    <version>0.2.2</version>
</dependency>

```

### Gradle

```groovy
implementation group: 'com.neko233', name: 'sql-light-rail', version: '0.2.2'
```

## 初衷 / 痛点

因 mybatis-plus/flux 等好用的 ORM 框架, 都依赖于 mybatis. 

但一旦离开了 mybatis 生态圈, 很多好用的机制不能拿出来独立使用. 

1. 独立的 SQL stream 写法, 直接生成 sql 语句. 无需 mybatis-plus 重量级依赖.
2. 独立的 ORM 机制.
3. 独立的 Sharding DB 机制
4. Plugin Chain 处理 SQL 执行过程.


# Use
Dependency
maven
```xml
<!-- ORM -->
<dependency>
    <groupId>com.neko233</groupId>
    <artifactId>sqlContext-light-rail</artifactId>
    <version>0.2.2</version>
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

Java
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

    /**
     * How to use multi DataSource
     * @throws Exception 异常
     */
    @Test
    public void multiDataSourceTest() throws Exception {
        // 多个 dataSource
        RepositoryManager repositoryManager = RepositoryManagerFactory.create(ds);
        repositoryManager.addDataSource("ds1", ds1);

        System.out.println("--------- sql_light_rail 数据源 -------------- ");
        String sql = SqlLightRail.selectTable(User.class)
                .limitByPage(1, 1)
                .build();
        List<User> users = repositoryManager.executeQuery(SqlStatement.builder()
            .sql(sql)
            .returnType(User.class)
            .build());
        users.forEach(System.out::println);

        System.out.println("--------- sql_light_rail_1 数据源 -------------- ");
        List<User> otherUsers = repositoryManager.executeQuery(SqlStatement.builder()
            .shardingKey("ds1")
            .sql(SqlLightRail.selectTable(User.class)
                .limit(0, 1)
                .build())
            .returnType(User.class)
            .build());
        otherUsers.forEach(System.out::println);
    }
```


