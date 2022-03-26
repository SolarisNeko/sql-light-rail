# SQL Light Rail

## 简介
> 'sql-light-rail' is a DAO Layer Micro Framework to handle SQL by Java, use it like 'Chain Builder / Stream'.
>
> '轻轨' 是一款 DAO 层操作的微框架, 处理 SQL 就如同写 Java Stream / Chain Builder 一样。

搭配 IDE 提示，你能几乎提速 200%+ 的编写 SQL 速度，并且能够借助 SQL Light Rail 避免大量的低级语法错误。

同时，将 SQL 难以编写的复杂语法，转移到 Java 层面处理，实在是非常的爽！


### 详细介绍
SQL Light Rail (SQL 轻轨)
        
轻轨: 有多节车厢, 如同本框架设计的 Chain Builder 思想。

这是一款【约定大于配置】的 Java SQL Flux 框架, 用于快速构建大量 SQL, 辅助 DAO 操作/复用 SQL。

设计原因:
1. 因部分公司有大量历史代码, MyBatis 入侵性过强, 迁移的工作量巨大, 并不能友好地支持快速开发. 
2. 其次, MyBatis Generator 生成无意义的代码过多, 文件过多, 项目结构庞大, 不够轻量化。 
3. 以及...原因。
4. 我希望复杂拼接 SQL 应该在 Java 层, 而不是在 xml .并且配合 IDE 智能提示, 如 IDEA, vscode, 等 都有明显的开发速度提升。

约定 > 配置:
1. 如果你的 Pojo 遵循驼峰命名, SQL Table 命名遵循大驼峰命名, 如 class LoginSumDaily -> table login_sum_daily 。 那么我们会自动帮你将 field 转换为表结构。
2. 采用 Flux/Stream 写法。

本架构 License 为 Apache2.0

## Download
### Maven
```xml
<dependency>
    <groupId>com.neko233</groupId>
    <artifactId>sql-light-rail</artifactId>
    <version>0.0.4</version>
</dependency>

```

### Gradle
```groovy
implementation group: 'com.neko233', name: 'sql-light-rail', version: '0.0.3'
```

## 初衷 / 痛点
1.  MyBatis 迁移的工作量巨大, 除非重构项目, 实际开发速度提升并不明显。
2. MyBatis 并没有足够好到让我替换巨量DAO操作，入侵性过强，较为重量。
3. MyBatis 不太兼容很多公司自研框架 (例如：分库分表框架等, 限死了使用 ShardingSphere/MyCat 等).
4. 没有语法提示。工作中使用到的 SQL 非常多, 但语法也各不一样，如: MySQL, Oracle, Spark SQL, Hive SQL, Kylin SQL...

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
String selectSql = SqlLightRail.selectTable("user a")
        .select("a.id", "a.name")
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
String name = "root";

// 新手不要学，纯粹演示用
SelectSqlBuilder sqlBuilder = SqlLightRail.selectTable(User.class);
if ("root".equals(name)) {
    sqlBuilder.where(WhereCondition.builder()
        .equalsTo("deleted", "0")
        .lessThanOrEquals("create_time", new Date())
    );
}
if ("sb".equals(name)) {
    sqlBuilder.where(WhereCondition.builder()
        .equalsTo("name", "root")
        .equalsTo("1", "1")
    );
}

String selectSql = sqlBuilder.build();
// SELECT id, name FROM user WHERE deleted = '0' and create_time <= '2022-02-27 12:04:58'
System.out.println(selectSql);
```

# 完成进度
## 1、基本进度
1. [x] 基本 CRUD
2. [ ] MySQL Partition 分区 API 支持


## 2、细进度
### 1、Insert
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

### 2. Select
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
String selectAllSql = SqlLightRail.selectTable(User.class).build();
String target = "SELECT id, name FROM user ";
```
2. 


### 3、Update
1. [x] Update table
2. [x] set col1 = ?, col2 = ?
3. [x] Where col3 = ? and col4 = ?

### 4、Delete
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
DataSource dataSource = DruidDataSourceFactory.createDataSource(getDbConfig());

// Demo
RailPlatform railPlatform = RailPlatformFactory.createLightRailPlatform(dataSource);
SqlBuilder updateSql = SqlLightRail.updateTable("user")
    .set("create_time = '2022-01-01 11:11:11'")
    .where(WhereCondition.builder()
        .equalsTo("id", 1)
    );
Integer rowCount = railPlatform.executeUpdate(updateSql);
Assert.assertTrue(1 == rowCount);
```
