package com.neko233.sql.lightrail.plugin;

import com.neko233.sql.lightrail.domain.ExecuteSqlContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SolarisNeko
 * Date on 2022-02-26
 */
@Slf4j
public class LogSqlPlugin extends Plugin {

    private static final String LOG_PREFIX_TITLE = "[Plugin|PrintSql] ";

    public LogSqlPlugin() {
        super("slow-sql-plugin");
    }

    @Override
    public void initPlugin() {
        log.info("[sql-light-rail] register 'print-sql-plugin' successfully! ");
    }

    @Override
    public void begin() {

    }

    @Override
    public void preExecuteSql(ExecuteSqlContext context) {
        String multiSql = String.join("\n", context.getSqlList());
        log.info(LOG_PREFIX_TITLE + "db = " + context.getDbName() + " | SQL = " + multiSql);
    }

    @Override
    public void end() {
        super.end();
    }

}
