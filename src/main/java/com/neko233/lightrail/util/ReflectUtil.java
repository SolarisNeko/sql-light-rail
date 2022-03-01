package com.neko233.lightrail.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author SolarisNeko
 * Date on 2022-02-20
 */
@Slf4j
public class ReflectUtil {

    public static final String SUPER_CLASS_SIMPLE_NAME = "Object";
    public static final Class SUPER_CLASS = Object.class;

    public static Object getFieldValueByNameShortly(Object object, String insertColumn) {
        Field field;
        Field parentField = null;
        Field childField = null;
        Object fieldValue = null;
        Class<?> targetClass = object.getClass();

        // 1 get which field
        try {
            parentField = getParentFieldByNameShortly(targetClass, insertColumn);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            childField = targetClass.getDeclaredField(insertColumn);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        // parent field not exists
        field = childField != null ? childField : parentField;
        // 2 get value
        if (field == null) {
            return null;
        }
        try {
            field.setAccessible(true);
            fieldValue = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }

    /**
     * 递归查找父类最近的同名 field .
     */
    private static Field getParentFieldByNameShortly(Class<?> targetClass, String insertColumn) throws NoSuchFieldException {
        Field parentField = null;
        Class<?> superclass = targetClass.getSuperclass();
        while (parentField == null && !superclass.getSimpleName().equals(Object.class.getSimpleName())) {
            parentField = superclass.getDeclaredField(insertColumn);
        }
        return parentField;
    }

    public static List<String> getFieldNames(Class aClass) {
        List<String> columns = new ArrayList<>();
        while (!(aClass.getSimpleName().equals(SUPER_CLASS_SIMPLE_NAME))) {
            List<String> currentFields = Arrays.stream(aClass.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());
            columns.addAll(currentFields);
            aClass = aClass.getSuperclass();
        }
        return columns.stream().sorted().collect(toList());
    }

    public static List<Field> getAllFields(Class aClass) {
        List<Field> columns = new ArrayList<>();
        while (!(aClass.getSimpleName().equals(SUPER_CLASS_SIMPLE_NAME))) {
            List<Field> currentFields = Arrays.stream(aClass.getDeclaredFields())
                .collect(Collectors.toList());
            columns.addAll(currentFields);
            aClass = aClass.getSuperclass();
        }
        return columns.stream().collect(toList());
    }


    public static <T> List<String> getFieldNames(T object) {
        List<String> columns = new ArrayList<>();
        Class<?> aClass = object.getClass();
        while (!(aClass.getSimpleName().equals(SUPER_CLASS_SIMPLE_NAME))) {
            List<String> currentFields = Arrays.stream(aClass.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());
            columns.addAll(currentFields);
            aClass = aClass.getSuperclass();
        }
        return columns.stream().sorted().collect(toList());
    }
}