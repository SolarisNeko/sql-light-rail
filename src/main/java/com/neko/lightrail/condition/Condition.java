package com.neko.lightrail.condition;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
public interface Condition {

    /**
     * @return 条件构建后的 SQL String 。
     */
    String build();


    String PLACEHOLDER = "?";

    /**
     * 根据 value 的类型, 返回 SQL 中的 value 如何呈现
     */
    public static String toSqlValueByType(Object value) {
        if (value == null) {
            return "null";
        }
        if (String.valueOf(value).equals(PLACEHOLDER)) {
            return "?";
        }
        if (value instanceof CharSequence) {
            return "'" + value + "'";
        }
        return String.valueOf(value);
    }


}
