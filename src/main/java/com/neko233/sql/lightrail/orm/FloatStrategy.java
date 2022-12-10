package com.neko233.sql.lightrail.orm;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author SolarisNeko
 * @date 4/23/2022
 */
public class FloatStrategy implements ConvertStrategy {
    @Override
    public Object singleColumn(ResultSet thisRowRs) throws IllegalAccessException, InstantiationException, SQLException {
        return thisRowRs.getFloat(1);
    }

    @Override
    public Object columnNameConvert(ResultSet thisRowRs, String columnName) throws SQLException {
        return thisRowRs.getFloat(columnName);
    }
}
