package com.neko233.sql.lightrail.orm;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author SolarisNeko
 * @date 4/23/2022
 */
public class IntegerStrategy implements ConvertStrategy {
    @Override
    public Object singleColumn(ResultSet thisRowRs) throws IllegalAccessException, InstantiationException, SQLException {
        return thisRowRs.getInt(1);
    }

    @Override
    public Object columnNameConvert(ResultSet thisRowRs, String columnName) throws SQLException {
        return thisRowRs.getInt(columnName);
    }
}
