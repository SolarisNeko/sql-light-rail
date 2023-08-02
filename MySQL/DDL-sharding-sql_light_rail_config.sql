-- first init
-- 首次初始化！
create database if not exists sql_light_rail_config;
use sql_light_rail_config;

drop table if exists neko233_db_group_config_template;
create table if not exists neko233_db_group_config_template
(
    id             bigint auto_increment primary key,
    group_name     varchar(64)            not null,
    template_key   varchar(64)            not null,
    template_value text                   not null,
    add_dt         datetime default now() not null,
    unique key (group_name, template_key)
    ) charset = utf8mb4;
INSERT ignore INTO sql_light_rail_config.neko233_db_group_config_template (group_name, template_key, template_value)
VALUES ('template', 'username', 'root');
INSERT ignore INTO sql_light_rail_config.neko233_db_group_config_template (group_name, template_key, template_value)
VALUES ('template', 'minIdle', '5');
INSERT ignore INTO sql_light_rail_config.neko233_db_group_config_template (group_name, template_key, template_value)
VALUES ('template', 'initialSize', '5');
INSERT ignore INTO sql_light_rail_config.neko233_db_group_config_template (group_name, template_key, template_value)
VALUES ('template', 'maxWait', '10000');
INSERT ignore INTO sql_light_rail_config.neko233_db_group_config_template (group_name, template_key, template_value)
VALUES ('template', 'maxActive', '10');
INSERT ignore INTO sql_light_rail_config.neko233_db_group_config_template (group_name, template_key, template_value)
VALUES ('template', 'password', 'root');
INSERT ignore INTO sql_light_rail_config.neko233_db_group_config_template (group_name, template_key, template_value)
VALUES ('template', 'url', 'jdbc:mysql://localhost:3306/${database}?${urlParameter}');



drop table if exists neko233_db;
create table if not exists neko233_db
(
    id         bigint auto_increment primary key,
    group_name varchar(64)            not null,
    db_id      int                    not null,
    tag        varchar(64)            not null comment '标签, use | split',
    add_dt     datetime default now() not null,
    unique key (group_name, db_id)
    ) charset = utf8mb4;
INSERT ignore INTO sql_light_rail_config.neko233_db (group_name, db_id, tag)
VALUES ('template', 0, 'template|not_exists');
INSERT ignore INTO sql_light_rail_config.neko233_db (group_name, db_id, tag)
VALUES ('template', 1, 'template|not_exists');


drop table if exists neko233_tag_config_kv;
create table if not exists neko233_tag_config_kv
(
    id           bigint auto_increment primary key,
    tag          varchar(64),
    config_key   varchar(64) not null,
    config_value text        not null,
    add_dt       datetime    not null default now(),
    unique key (tag, config_key)
    ) charset = utf8mb4;
INSERT ignore INTO sql_light_rail_config.neko233_tag_config_kv (tag, config_key, config_value)
VALUES ('template', 'database', 'sql_light_rail_config');
INSERT ignore INTO sql_light_rail_config.neko233_tag_config_kv (tag, config_key, config_value)
VALUES ('template', 'urlParameter', 'charset=utf8');


drop table if exists neko233_db_sharding_strategy;
create table if not exists neko233_db_sharding_strategy
(
    group_name             varchar(64)            not null primary key,
    sharding_strategy_name varchar(64)            not null comment 'db分片策略名(database 分库)',
    is_use_default         bool     default true  not null comment '是否使用默认的分片策略(不db分片)',
    add_dt                 datetime default now() not null,
    unique key (group_name)
    ) charset = utf8mb4;

INSERT ignore INTO sql_light_rail_config.neko233_db_sharding_strategy (group_name, sharding_strategy_name, is_use_default) VALUES ('template', 'default', 1);
