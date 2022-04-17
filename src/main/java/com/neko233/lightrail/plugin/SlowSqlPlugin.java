package com.neko233.lightrail.plugin;

import com.neko233.lightrail.domain.ExecuteSqlContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SolarisNeko
 * Date on 2022-02-26
 */
@Slf4j
public class SlowSqlPlugin extends Plugin {

    private static final String LOG_PREFIX_TITLE = "[Plugin|SlowSql] ";
    private static final Long THRESHOLD_SLOW_SQL_IN_MS = 1000L;

    public static final Long NANO_TO_MILLIS_SECONDS = 1_000_000L;

    /**
     * ThreadLocal for temporary plugin.
     */
    private static final ThreadLocal<Long> START_MS_TIME_THREAD_LOCALS = new ThreadLocal<>();

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
        long startMsTime = System.nanoTime() / NANO_TO_MILLIS_SECONDS;
        START_MS_TIME_THREAD_LOCALS.set(startMsTime);
    }

    @Override
    public void postExecuteSql(ExecuteSqlContext context) {
        checkIsSlowSql(context.getSql(), START_MS_TIME_THREAD_LOCALS.get());
    }

    @Override
    public void end() {
        super.end();
    }


    /**
     * 特殊功能
     *
     * @param startMsTime
     * @param sql
     */
    private static void checkIsSlowSql(String sql, long startMsTime) {
        long spendTime = getCurrentMsTime() - startMsTime;
        // must remove after use
        START_MS_TIME_THREAD_LOCALS.remove();
        if (spendTime > THRESHOLD_SLOW_SQL_IN_MS) {
            log.warn(LOG_PREFIX_TITLE + "Slow SQL warn. SQL = {}. Spend Time = {} ms", sql, spendTime);
            return;
        }
        log.info(LOG_PREFIX_TITLE + "Query SQL = {}. Spend Time = {} ms", sql, spendTime);
    }

    private static long getCurrentMsTime() {
        return System.nanoTime() / NANO_TO_MILLIS_SECONDS;
    }

}
