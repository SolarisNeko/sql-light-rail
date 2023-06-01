package com.neko233.sql.lightrail.util;

import com.neko233.sql.lightrail.annotation.Column;
import com.neko233.sql.lightrail.annotation.IgnoreColumn;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author SolarisNeko
 * Date on 2022-12-17
 */
public class SqlColumnUtil {

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


    public static <T> Map<String, Field> getColumnName2FieldMap(Class<T> clazz) {
        Map<String, Field> haveAnnoFields = ORM_CACHE_MAP.get(clazz);
        if (haveAnnoFields == null) {
            haveAnnoFields = refreshOrmFields(clazz);
        }
        return haveAnnoFields;
    }


    /**
     * 获取 column -> field 的 Map, 先到先得匹配
     *
     * @param returnClazz 返回类型
     * @return 字段
     */
    public static Map<String, Field> getColumnName2FieldMapByFirst(Class<?> returnClazz) {

        List<Field> allReturnClass = ReflectUtil.getAllFields(returnClazz);

        Map<String, Field> columnName2FieldMap = allReturnClass.stream().filter(field -> {
                    // 忽略
                    IgnoreColumn annotationForIgnoreColumn = field.getAnnotation(IgnoreColumn.class);
                    if (annotationForIgnoreColumn != null) {
                        return false;
                    }

                    Column annotation = field.getAnnotation(Column.class);
                    if (annotation == null) {
                        return true;
                    }
                    // 空的, 不管
                    if (StringUtils.isBlank(annotation.value())) {
                        return false;
                    }
                    // 不使用了, 也丢弃
                    return annotation.isUse();
                })
                // columnName : Field
                .collect(Collectors.toConcurrentMap(field -> {
                    Column annotation = field.getAnnotation(Column.class);
                    // 无注释, 则为 userName -> user_name
                    if (annotation == null) {
                        return CamelCaseUtil.getBigCamelLowerName(field.getName());
                    }
                    return annotation.value();
                }, field -> field, (v1, v2) -> v2));
        return columnName2FieldMap;
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

        final Map<String, Field> columnName2FieldMap = SqlColumnUtil.getColumnName2FieldMapByFirst(returnClazz);

        ORM_CACHE_MAP.put(returnClazz, columnName2FieldMap);
        return columnName2FieldMap;
    }

}
