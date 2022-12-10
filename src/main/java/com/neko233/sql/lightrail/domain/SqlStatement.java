package com.neko233.sql.lightrail.domain;

import com.neko233.sql.lightrail.plugin.Plugin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行一次 SQL 的装配对象
 *
 * @author SolarisNeko
 * Date on 2022-03-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SqlStatement {

    private String shardingKey;

    private String sql;

    private List<String> sqlList;

    private Class<?> returnType;

    private Boolean isAutoCommit;

    private List<Plugin> addTempPlugins;

    private List<String> excludePluginNames;

}
