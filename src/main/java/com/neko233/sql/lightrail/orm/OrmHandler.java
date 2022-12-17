package com.neko233.sql.lightrail.orm;

import com.neko233.sql.lightrail.util.SqlColumnUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LuoHaoJun on 2022-12-07
 **/
@Slf4j
public class OrmHandler {

    public static <T> List<T> ormBatch(ResultSet resultSet, Class<T> returnClass) throws SQLException {
        Map<String, Field> haveAnnoFields = SqlColumnUtil.getColumnName2FieldMap(returnClass);

        ConvertStrategy convertStrategy = ConvertStrategyFactory.chooseStrategyByReturnType(returnClass);

        List<T> list = new ArrayList<>();
        while (resultSet.next()) {
            try {
                T newObject = (T) convertStrategy.autoMatch(resultSet, null, returnClass, haveAnnoFields);
                list.add(newObject);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("orm batch happen error. ", e);
            }
        }
        return list;
    }

    public static <T> T ormSingle(ResultSet resultSet, Class<T> returnClass) throws SQLException {
        Map<String, Field> haveAnnoFields = SqlColumnUtil.getColumnName2FieldMap(returnClass);

        ConvertStrategy convertStrategy = ConvertStrategyFactory.chooseStrategyByReturnType(returnClass);

        if (resultSet.next()) {
            try {
                return (T) convertStrategy.autoMatch(resultSet, null, returnClass, haveAnnoFields);

            } catch (InstantiationException | IllegalAccessException e) {
                log.error("orm single happen error. ", e);
            }
        }
        return null;
    }

}
