package com.neko233.lightrail.plugin;

import com.neko233.lightrail.domain.ExecuteSqlContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SolarisNeko
 * Date on 2022-02-26
 */
@Slf4j
public class PrintSqlPlugin extends Plugin {

    private static final String LOG_PREFIX_TITLE = "[Plugin|PrintSql] ";

    public PrintSqlPlugin() {
        super("slow-sql-plugin");
    }

    @Override
    public void initPlugin() {
        log.info("[LightRailPlugin] register 'print-sql-plugin' successfully! ");
    }

    @Override
    public void begin() {

    }

    @Override
    public void preExecuteSql(ExecuteSqlContext context) {
        String multiSql = String.join("\n", context.getSqlList());
        log.info(LOG_PREFIX_TITLE + "shardingKey = " + context.getShardingKey() + " | SQL = " + multiSql);
    }

    @Override
    public void end() {
        super.end();
    }

}
