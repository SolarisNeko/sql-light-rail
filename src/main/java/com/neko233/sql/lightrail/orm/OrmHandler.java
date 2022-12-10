package com.neko233.sql.lightrail.orm;

import com.neko233.sql.lightrail.annotation.Column;
import com.neko233.sql.lightrail.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author LuoHaoJun on 2022-12-07
 **/
@Slf4j
public class OrmHandler {

    private static final Map<Class<?>, Map<String, Field>> ORM_CACHE_MAP = new ConcurrentHashMap<>();

    public static <T> List<T> ormBatch(ResultSet resultSet, Class<T> returnClass) throws SQLException {
        Map<String, Field> haveAnnoFields = getOrCreateFieldNameListWithAnnoColumn(returnClass);

        ConvertStrategy convertStrategy = ConvertStrategyFactory.chooseStrategyByReturnType(returnClass);

        List<T> list = new ArrayList<>();
        while (resultSet.next()) {
            try {
                T newObject = (T) convertStrategy.autoMatch(resultSet, null, returnClass, haveAnnoFields);
                list.add(newObject);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    public static <T> T ormSingle(ResultSet resultSet, Class<T> returnClass) throws SQLException {
        Map<String, Field> haveAnnoFields = getOrCreateFieldNameListWithAnnoColumn(returnClass);

        ConvertStrategy convertStrategy = ConvertStrategyFactory.chooseStrategyByReturnType(returnClass);

        if (resultSet.next()) {
            try {
                return (T) convertStrategy.autoMatch(resultSet, null, returnClass, haveAnnoFields);

            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    private static <T> Map<String, Field> getOrCreateFieldNameListWithAnnoColumn(Class<T> clazz) {
        Map<String, Field> haveAnnoFields = ORM_CACHE_MAP.get(clazz);
        if (haveAnnoFields == null) {
            haveAnnoFields = refreshOrmFields(clazz);
        }
        return haveAnnoFields;
    }

    private synchronized static <T> Map<String, Field> refreshOrmFields(Class<T> returnClazz) {
        List<Field> allReturnClass = ReflectUtil.getAllFields(returnClazz);

        Map<String, Field> columnName2FieldMap = allReturnClass.stream()
                .filter(field -> field.getAnnotation(Column.class) != null)
                .collect(Collectors.toConcurrentMap(
                        haveAnnoField -> {
                            Column annotation = haveAnnoField.getAnnotation(Column.class);
                            return annotation.value();
                        },
                        field -> field,
                        (v1, v2) -> v2
                ));


        ORM_CACHE_MAP.put(returnClazz, columnName2FieldMap);
        return columnName2FieldMap;
    }

}
