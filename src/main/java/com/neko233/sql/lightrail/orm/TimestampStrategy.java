package com.neko233.sql.lightrail.orm;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author SolarisNeko
 * @date 4/23/2022
 */
public class TimestampStrategy implements ConvertStrategy {

    @Override
    public Object singleColumn(ResultSet thisRowRs) throws IllegalAccessException, InstantiationException, SQLException {
        return thisRowRs.getTimestamp(1);
    }

    @Override
    public Object columnNameConvert(ResultSet thisRowRs,
                                    String columnName) throws SQLException, IllegalAccessException, InstantiationException {
        return thisRowRs.getTimestamp(columnName);
    }


}
