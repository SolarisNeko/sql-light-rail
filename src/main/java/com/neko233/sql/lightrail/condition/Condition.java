package com.neko233.sql.lightrail.condition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
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

    DateFormat yyyyMMdd_hhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static String toSqlValueByType(Object value) {
        if (value == null) {
            return "null";
        }
        if (String.valueOf(value).equals(PLACEHOLDER)) {
            return "?";
        }

        if (value instanceof CharSequence) {
            return "'" + String.valueOf(value).replaceAll("'", "''") + "'";
        }
        if (value instanceof Date) {
            return "'" + yyyyMMdd_hhmmss.format(value) + "'";
        }
        return String.valueOf(value);
    }


}
