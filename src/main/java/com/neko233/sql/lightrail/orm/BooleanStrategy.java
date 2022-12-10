package com.neko233.sql.lightrail.orm;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author SolarisNeko
 * @date 4/23/2022
 */
public class BooleanStrategy implements ConvertStrategy {
    @Override
    public Object singleColumn(ResultSet nextRs) throws IllegalAccessException, InstantiationException, SQLException {
        return nextRs.getBoolean(1);
    }

    @Override
    public Object columnNameConvert(ResultSet thisRowRs, String columnName) throws SQLException {
        return thisRowRs.getBoolean(columnName);
    }
}
