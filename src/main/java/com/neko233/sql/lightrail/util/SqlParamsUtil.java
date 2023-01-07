package com.neko233.sql.lightrail.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author SolarisNeko on 2022-12-09
 **/
public class SqlParamsUtil {

    public static boolean setParams(PreparedStatement ps, Object[] params) throws SQLException {
        if (params == null) {
            return false;
        }
        for (int i = 0; i < params.length; i++) {
            setSingleParam(ps, i + 1, params[i]);
        }
        return true;
    }

    /**
     *
     * @param ps SQL PreparedStatement
     * @param paramIndex start from 1
     * @param param value
     * @throws SQLException exception
     */
    private static void setSingleParam(PreparedStatement ps, int paramIndex, Object param) throws SQLException {
        if (param == null) {
            // Oracle 低版本不支持 null
            ps.setNull(paramIndex, java.sql.Types.VARCHAR);
            // stmt.setNull(parameterIndex, java.sql.Types.NULL);
        } else {
            ps.setObject(paramIndex, param);
        }
    }

}
