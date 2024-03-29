# RoadMap - SQL Light Rail

## v1.1.0

1. [Update] 整理代码. 调整各个包名.
2. [Deprecated] 将 class SqlLightRail 标记废弃 --> SqlBuilder233. 名字更好记一点
3. [Update] 更新 ShardingIdStrategy 计算的策略签名. 基于 string 计算
4. [Update] 依赖库更新 com.neko233:skilltree-commons-core -> 0.1.2
5. [Delete] 删除冗余的代码, 减少代码量
6. [Delete] 彻底移除 snake-yaml 老是有安全问题真的是无语了. 后续再考虑使用 yaml 配置.

## v1.0.2

1. [Add] 追加了 @IgnoreColumn 忽略字段
2. [Add] 修复了 InsertBuilder 一些小问题
3. [Add] Db 追加了原生自由控制的 connection Consumer
4. [Add] 追加了 ORM 类型 Timestamp, LocalDateTime 等
5. [Update] InsertBuilder 自动生成 insert sql 的方法重命名为 orm

## v1.0.1
1. [Delete] 删减了snakeYaml 的使用

## v1.0.0

1. [Add] 正式发布

## v0.3.1

1. [Add] add ConditionGenerator for SQL 'in'
2. [BugFix] tag split by '|'
3. [Update] DDL-sharding-sql_light_rail_config.sql for Test

## v0.3.0

v0.2.2 -> v0.3.0

1. [Refactor] refactor class RepositoryManager / manager layer.
2. [Add] Layer = DbGroup / Db / DbShardingStrategy / ....
3. [Add] add more sql original params use.
4. [Add] Druid as default DataSource
5. [Add] Add Type support LocalDateTime as Mysql:DateTime type
6. [BugFix] base type in Orm will happen some error.
7. [Add] not support Base Type in 'ObjectStrategy'

## 0.2.2

1. [Optimize] 优化了反射部分性能, 提升效率额 90%. 从 100ms -> 6~20 ms. 加入 lazy cache.
2. [Add] Insert SQL 的 columnName 进行了 @Column 的命名支持.
3. [Add] 完善单元测试 for ORM.

## v0.2.1

1. [Rename] RailPlatform -> RepositoryManager
2. [Add] RepositoryGroupManager 组管理器
3. [Add] 独立 Sharding 机制出来, ShardingId: Number + ShardingKey: String.

## v0.2.0

1. ORM 部分重构.

## v0.1.1

1. 修复 InsertSqlBuilder 问题
2. 追加 on duplicate key update 特性
3. 分离 insert 的 singleRowValue 单行插入（该框架基于批处理思想）
4. 确定历史拼接逻辑，过于臃肿难以优化。后续会更改拼接内核。不影响外部 API

## v0.1.0

基本可用.

## Development Status [发展状态]

Base function done !

基础功能已全部完成 ！

Aha~ We're glad to you use our Architecture to avoid Write SQL in XML / String.

我们很高兴你可以使用我们的 SQL Light Rail 去避免大量的将 SQL String 迁移到 XML / 保持原来的 SQL String 拼接。

> Advanced Function [高级功能]

高级功能1 : 主要打造 LightRail Platform，将

1. SQL 生成
2. SQL 执行
3. ResultSet 结果映射

融为一体。

高级功能2 : 简单适配 BigData 领域的特殊 SQL

1. Hive 的 Delimiter By ... Row Formatter .... 等 SQL 语法。

> Special Function [特殊功能]

yml config

# Code & API

## Code Guide

[详细手册 Code Guide](./Code%20Guide.md)

## Simpel Demo

Select

```java
// SELECT a.id, a.name FROM user a JOIN user_role b ON a.id = b.id 
String selectSql=Sql233.selectTable("user a")
        .select("a.id","a.name")
        .join(JoinCondition.builder()
        .join("user_role b")
        .on("a.id = b.id")
        )
        .build();
```

应对复杂业务的情况

```java
// 1 Base SQL
// if Web send you some data
String name="root";

// 新手不要学，纯粹演示用
        SelectSqlBuilder sqlBuilder=Sql233.selectTable(User.class);
        if("root".equals(name)){
        sqlBuilder.where(WhereCondition.builder()
        .equalsTo("deleted","0")
        .lessThanOrEquals("create_time",new Date())
        );
        }
        if("sb".equals(name)){
        sqlBuilder.where(WhereCondition.builder()
        .equalsTo("name","root")
        .equalsTo("1","1")
        );
        }

        String selectSql=sqlBuilder.build();
// SELECT id, name FROM user WHERE deleted = '0' and create_time <= '2022-02-27 12:04:58'
        System.out.println(selectSql);
```

# 完成进度

## 1、基本进度

1. [x] 基本 CRUD
2. [ ] MySQL Partition 分区 API 支持

## 2、细进度

## 1、Insert

Base Function

1. [x] insert into ...
2. [x] table(col1, col2)
3. [x] values auto generate by valueList
4. [x] values by "SQL String"

Advanced Function

1. [x] ORM generate Insert。
   | 通过 object 自动映射生成 Insert 语句。
2. [x] auto generate by pojo ORM to (?, ?) any times.
   | 自动生成占位符模板任意次。

## 2. Select

基本功能 :

1. [x] select
2. [x] alias Map
3. [x] from
4. [x] where
5. [x] order by
6. [x] group by
7. [x] limit
8. [x] limit by pageNum, pageSize
9. [x] inner Select
10. [x] join ... on ...

进阶功能

1. ORM 映射查询, 直接查询全部 field

高级功能:

1. ORM 查询, 默认查询所有字段

```java
// User 对象只有 id, name 两个 fields
String selectAllSql=Sql233.selectTable(User.class).build();
        String target="SELECT id, name FROM user ";
```

2.

## 3、Update

1. [x] Update table
2. [x] set col1 = ?, col2 = ?
3. [x] Where col3 = ? and col4 = ?

## 4、Delete

1. [x] Delete From table
2. [x] where ...

# DAO Unified Layer - RailPlatform

> Rail Platform, Dispatch SQL how to work.
>
> 轻轨系统平台, 指挥 SQL 执行。

RailPlatform 由 4 部分组合而成:

1. SqlLightRail : 这是一款轻量级的 Java SQL 编写工具。主要处理各类 Java 缘分
2. JDBC : 不解释。
3. LightRailPlugin : 执行 SQL 生命周期的插件。不清楚可以见 SlowSqlPlugin (慢查询统计).
4. LightRailOrm : 对象映射

## 入口

RailPlatformFactory

你需要自己准备 DataSource (数据源 / 数据库连接池) 给本框架。

# RailPlatform - Code & API

## How to use

```java
// 'MyDataSource.getDataSource()' need you @override it!
// DataSource 需要你自己提供, 如: Druid, C3P0, Hikari ...
DataSource dataSource=DruidDataSourceFactory.createDataSource(getDbConfig());

// Demo
        RailPlatform repositoryManager=RailPlatformFactory.createLightRailPlatform(dataSource);

        // 基本类型
        List<Integer> testIntValue=repositoryManager.executeQuery("Select 1 From dual ",Integer.class);
        // must not null
        System.out.println(testIntValue.get(0));

        // Object ORM
        List<User> users=repositoryManager.executeQuery("Select id, name From user Limit 0, 1 ",User.class);
        users.forEach(System.out::println)

```

## Batch Update SQL

1. support Update SQL
2. non-support Select SQL

```java
    @Test
public void updateMultiSql_2_successfully()throws Exception{
        RailPlatform repositoryManager=RailPlatformFactory.createLightRailPlatform(MyDataSource.getDefaultDataSource());

        String build=Sql233.updateTable("user")
        .set(SetCondition.builder().equalsTo("create_time",new Date()))
        .where(WhereCondition.builder()
        .equalsTo("id",1)
        ).build();
        String build2=Sql233.updateTable("user")
        .set(SetCondition.builder().equalsTo("create_time",new Date()))
        .where(WhereCondition.builder()
        .equalsTo("id",1)
        ).build();

        Integer rowCount=repositoryManager.executeUpdate(Arrays.asList(build,build2));

        Assert.assertTrue(2==rowCount);
        }
```