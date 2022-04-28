package com.neko233.lightrail.plugin;

import com.neko233.lightrail.domain.ExecuteSqlContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author SolarisNeko
 * Date on 2022-02-26
 */
@Slf4j
public class SlowSqlPlugin extends Plugin {

    private static final String LOG_PREFIX_TITLE = "[Plugin|SlowSql] ";
    private static final Long THRESHOLD_SLOW_SQL_IN_MS = 1000L;

    /**
     * ThreadLocal for temporary plugin.
     */
    private static final ThreadLocal<StopWatch> START_MS_TIME_THREAD_LOCALS = new ThreadLocal<>();

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
        START_MS_TIME_THREAD_LOCALS.remove();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        START_MS_TIME_THREAD_LOCALS.set(stopWatch);
    }

    @Override
    public void postExecuteSql(ExecuteSqlContext context) {
        List<String> sqlList = context.getSql();
        StopWatch stopWatch = START_MS_TIME_THREAD_LOCALS.get();
        stopWatch.stop();
        checkIsSlowSql(String.join("\n", sqlList), stopWatch.getTime(TimeUnit.MILLISECONDS));
    }

    @Override
    public void end() {
        super.end();
    }


    /**
     * 特殊功能
     *
     * @param spendMsec 使用的毫秒
     * @param sql 执行的 Sql
     */
    private static void checkIsSlowSql(String sql, long spendMsec) {
        // must remove after use
        if (spendMsec > THRESHOLD_SLOW_SQL_IN_MS) {
            log.warn(LOG_PREFIX_TITLE + "Slow SQL warn. SQL = {}. Spend Time = {} ms", sql, spendMsec);
        }
    }


}
