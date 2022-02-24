-- auto-generated definition
create table login_sum_daily
(
    id            int auto_increment
        primary key,
    user_sum      int        default 0 null,
    role_sum      int        default 0 null,
    device_sum    int        default 0 null,
    login_sum     int        default 0 null,
    register_time datetime             null,
    create_time   datetime             null,
    deleted       tinyint(1) default 0 null
);

