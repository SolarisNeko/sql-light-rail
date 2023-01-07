package com.neko233.sql.lightrail.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author SolarisNeko on 2022-12-07
 **/
public interface ResultSetHandler<T> {

    T handle(ResultSet resultSet) throws SQLException;

}
