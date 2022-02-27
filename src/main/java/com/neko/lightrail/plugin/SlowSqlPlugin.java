package com.neko.lightrail.plugin;

import com.neko.lightrail.domain.ExecuteSqlContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SolarisNeko
 * @date 2022-02-26
 */
@Slf4j
public class SlowSqlPlugin extends LightRailPlugin {

    public static final String LOG_PREFIX_TITLE = "[Slow-SQL] ";
    private static final Long THRESHOLD_SLOW_SQL_IN_MS = 1000L;

    public static final Long NANO_TO_MILLIS_SECONDS = 1_000_000L;
    private Long startMsTime;
    private Long endMsTime;


    public SlowSqlPlugin() {
        super("slow-sql-plugin");
    }

    @Override
    public void initPlugin() {
        log.info("[LightRailPlugin] register 'slow-sql-plugin' successfully! ");
    }

    @Override
    public void begin() {

    }

    @Override
    public void preExecuteSql(ExecuteSqlContext context) {
        startMsTime = System.nanoTime() / NANO_TO_MILLIS_SECONDS;
    }

    @Override
    public void postExecuteSql(ExecuteSqlContext context) {
        checkIsSlowSql(context.getSql(), startMsTime);
    }

    @Override
    public void finish() {
        super.finish();
    }


    /**
     * 特殊功能
     *
     * @param startMsTime
     * @param sql
     */
    private static void checkIsSlowSql(String sql, long startMsTime) {
        long spendTime = getCurrentNanoTime() - startMsTime;
        if (spendTime > THRESHOLD_SLOW_SQL_IN_MS) {
            log.warn(LOG_PREFIX_TITLE + "Slow SQL warn. SQL = {}. Spend Time = {} ms", sql, spendTime);
            return;
        }
        log.info(LOG_PREFIX_TITLE + "Query SQL = {}. Spend Time = {} ms", sql, spendTime);
    }

    private static long getCurrentNanoTime() {
        return System.nanoTime() / NANO_TO_MILLIS_SECONDS;
    }

}
