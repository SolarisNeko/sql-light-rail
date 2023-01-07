package com.neko233.sql.lightrail.condition.generator;

/**
 * SQL 操作符
 * @author LuoHaoJun on 2022-08-03
 **/
public enum SqlOperation {

    // =
    EQ,

    // !=
    NOT_EQ,

    // <
    LT,

    // <=
    LTE,

    // >
    GT,

    // >=
    GTE,

    // in ( ... )
    IN,

    // not in (..)
    NOT_IN,
}