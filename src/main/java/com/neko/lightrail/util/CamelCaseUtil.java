package com.neko.lightrail.util;


/**
 * @title 处理 char 工具
 * @description: 目前主要用来处理 [ Big Camel + Small Camel ]
 * @author: SolarisNeko
 * Date on: 2021/7/4
 */
public class CamelCaseUtil {

    /**
     * 转换成 Big Camel（大驼峰）的 Upper Case 版本!
     * 例如: SystemUser -> SYSTEM_USER
     */
    public static String getBigCamelUpperName(String name) {
        StringBuilder sb = new StringBuilder();
        char[] chars = name.toCharArray();
        if (chars.length != 0) {
            // 首字母不处理
            sb.append(chars[0]);
            // [1, n] 字母, 遇到大写, 进行大驼峰处理
            for (int i1 = 1; i1 < chars.length; i1++) {
                char aChar = chars[i1];
                final boolean upperCase = Character.isUpperCase(aChar);
                if (upperCase) {
                    // is Upper Case
                    sb.append("_").append(aChar);
                } else {
                    // is Lower Case
                    final char upperChar = Character.toUpperCase(aChar);
                    sb.append(upperChar);
                }
            }
        } else {
            throw new RuntimeException("命名, 转化成 Big Camel - Upper Case 异常!");
        }

        return sb.toString();
    }

    /**
     * 转换成 Big Camel（大驼峰）的 lower case 版本!
     * 例如: SystemUser -> system_user
     */
    public static String getBigCamelLowerName(String name) {
        StringBuilder sb = new StringBuilder();
        // 转化成 char[] 流
        char[] chars = name.toCharArray();
        if (chars.length != 0) {
            // 首字母不处理
            sb.append(Character.toLowerCase(chars[0]));
            // [1, n] 字母, 遇到大写, 进行大驼峰处理
            for (int i1 = 1; i1 < chars.length; i1++) {
                char aChar = chars[i1];
                boolean upperCase = Character.isUpperCase(aChar);
                if (upperCase) {
                    // Upper Case
                    sb.append("_" + Character.toLowerCase(aChar));
                } else {
                    // Lower Case
                    char upperChar = Character.toLowerCase(aChar);
                    sb.append(upperChar);
                }
            }
        } else {
            throw new RuntimeException("命名, 转化成 Big Camel - Lower Case 异常!");
        }

        return sb.toString();
    }
}

