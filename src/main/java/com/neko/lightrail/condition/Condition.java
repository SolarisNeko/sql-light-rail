package com.neko.lightrail.condition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
public interface Condition {

    /**
     * @return 条件构建后的 SQL String 。
     */
    String build();


    /**
     * 占位符
     * */
    String PLACEHOLDER = "?";

    DateFormat yyyyMMdd_hhmmss = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * 根据 value 的类型, 返回 SQL 中的 value 如何呈现
     * @return Java value -> SQL value 表现的 String 格式。
     */
    static String toSqlValueByType(Object value) {
        if (value == null) {
            return "null";
        }
        if (String.valueOf(value).equals(PLACEHOLDER)) {
            return "?";
        }
        if (value instanceof CharSequence) {
            return "'" + value + "'";
        }
        if (value instanceof Date) {
            return "'" + yyyyMMdd_hhmmss.format(value) + "'";
        }
        return String.valueOf(value);
    }


}
