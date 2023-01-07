package com.neko233.sql.lightrail.orm;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author SolarisNeko
 * @date 4/23/2022
 */
public class LocalDateTimeStrategy implements ConvertStrategy {

    @Override
    public Object singleColumn(ResultSet thisRowRs) throws IllegalAccessException, InstantiationException, SQLException {
        String dateTime = thisRowRs.getString(1);
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public Object columnNameConvert(ResultSet thisRowRs, String columnName) throws SQLException, IllegalAccessException, InstantiationException {
        String dateTime = thisRowRs.getString(columnName);
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }



}
