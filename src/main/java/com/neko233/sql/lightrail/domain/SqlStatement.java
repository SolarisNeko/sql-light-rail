package com.neko233.sql.lightrail.domain;

import lombok.*;

import java.util.List;

/**
 * 执行一次 SQL 的装配对象
 *
 * @author SolarisNeko
 * Date on 2022-03-06
 */
@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SqlStatement {

    private Boolean isQuery;

    private Boolean isAutoCommit;

    private List<String> sqlList;

    private Class<?> returnType;



}
