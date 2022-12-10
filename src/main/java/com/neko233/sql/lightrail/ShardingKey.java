package com.neko233.sql.lightrail;

/**
 * @author SolarisNeko
 * Date on 2022-04-17
 */
public class ShardingKey {

    /**
     * default sharding step = 2^21 = 104_8576
     * 默认设置一个 Table 100w+ 数据进行区分
     */
    private static Integer SHARDING_STEP = 104_8576;

    private static Boolean IS_CHANGE = false;

    public static Integer getShardingStep() {
        return SHARDING_STEP;
    }

    public static synchronized void modifyShardingStep(Integer shardingStep) {
        SHARDING_STEP = shardingStep;
        IS_CHANGE = true;
    }

    /**
     * sharding Key 加速的计算方法, 条件是 SHARDING_STEP 必须为 2^n 次方。
     *
     * @param autoIncrementId 用于计算 shardingKey 的数值, 一般为 id primary key auto_increment。
     *                        因为默认限制 104w, 没到 Integer 上限，不用担心。
     * @return shardingKey 的数值, 例如超过 104_8576
     */
    public static Integer calculateShardingKeySuffix(Integer autoIncrementId) {
        if (IS_CHANGE) {
            return autoIncrementId % SHARDING_STEP;
        }
        return autoIncrementId & (SHARDING_STEP - 1);
    }

}
