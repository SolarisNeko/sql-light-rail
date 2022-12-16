package com.neko233.sql.lightrail.orm;

import com.neko233.sql.lightrail.annotation.Column;
import com.neko233.sql.lightrail.util.CamelCaseUtil;
import com.neko233.sql.lightrail.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author LuoHaoJun on 2022-12-07
 **/
@Slf4j
public class OrmHandler {

    /**
     * ORM Cache
     */
    private static final Map<Class<?>, Map<String, Field>> ORM_CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * 排除的类名
     */
    private static final List<Class<?>> EXCLUDE_JDK_CLASS_LIST = new ArrayList<Class<?>>() {{
        add(Boolean.class);
        add(Short.class);
        add(Integer.class);
        add(Float.class);
        add(Double.class);
        add(Long.class);
        add(Byte.class);
        add(String.class);
        add(BigDecimal.class);
        add(Date.class);
    }};

    public static <T> List<T> ormBatch(ResultSet resultSet, Class<T> returnClass) throws SQLException {
        Map<String, Field> haveAnnoFields = getOrCreateFieldNameListWithAnnoColumn(returnClass);

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
        Map<String, Field> haveAnnoFields = getOrCreateFieldNameListWithAnnoColumn(returnClass);

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


    private static <T> Map<String, Field> getOrCreateFieldNameListWithAnnoColumn(Class<T> clazz) {
        Map<String, Field> haveAnnoFields = ORM_CACHE_MAP.get(clazz);
        if (haveAnnoFields == null) {
            haveAnnoFields = refreshOrmFields(clazz);
        }
        return haveAnnoFields;
    }

    /**
     * 刷新 ORM class 的字段到 cache 中
     *
     * @param returnClazz 返回类型
     * @param <T>         any
     * @return 返回的字段
     */
    private synchronized static <T> Map<String, Field> refreshOrmFields(Class<T> returnClazz) {
        if (EXCLUDE_JDK_CLASS_LIST.contains(returnClazz)) {
            return new HashMap<>();
        }

        List<Field> allReturnClass = ReflectUtil.getAllFields(returnClazz);

        Map<String, Field> columnName2FieldMap = allReturnClass.stream()
                .filter(field -> {
                    Column annotation = field.getAnnotation(Column.class);
                    if (annotation == null) {
                        return true;
                    }
                    // 空的, 不管
                    if (StringUtils.isBlank(annotation.value())) {
                        return false;
                    }
                    return annotation.isUse();
                })
                // columnName : Field
                .collect(Collectors.toConcurrentMap(
                        field -> {
                            Column annotation = field.getAnnotation(Column.class);
                            // 无注释, 则为 userName -> user_name
                            if (annotation == null) {
                                return CamelCaseUtil.getBigCamelLowerName(field.getName());
                            }
                            return annotation.value();
                        },
                        field -> field,
                        (v1, v2) -> v2
                ));


        ORM_CACHE_MAP.put(returnClazz, columnName2FieldMap);
        return columnName2FieldMap;
    }

}
