package com.neko233.sql.lightrail.util;


/**
 *  处理 char 工具
 * @author SolarisNeko
 * Date on: 2021/7/4
 */
public class CamelCaseUtil {


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

