package com.neko233.lightrail.strategy.type;

import com.neko233.lightrail.strategy.TypeStrategy;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author SolarisNeko
 * @date 4/23/2022
 */
public class StringStrategy implements TypeStrategy {
    @Override
    public Object getOrmValue(ResultSet nextRs, Class<?> returnType, List<Field> returnTypeFieldList, Map<String, String> field2ColumnNameMap) throws SQLException {
        return nextRs.getString(1);
    }
}
