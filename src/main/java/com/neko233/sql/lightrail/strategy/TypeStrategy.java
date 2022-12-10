package com.neko233.sql.lightrail.strategy;

import com.neko233.sql.lightrail.strategy.type.*;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author SolarisNeko
 * @date 4/23/2022
 */
public interface TypeStrategy {

    String INT = "int";
    String JAVA_LANG_INTEGER = "java.lang.Integer";
    String FLOAT = "float";
    String JAVA_LANG_FLOAT = "java.lang.Float";
    String DOUBLE = "double";
    String JAVA_LANG_DOUBLE = "java.lang.Double";
    String LONG = "long";
    String JAVA_LANG_LONG = "java.lang.Long";
    String JAVA_LANG_STRING = "java.lang.String";
    String JAVA_MATH_BIG_DECIMAL = "java.math.BigDecimal";
    String BOOLEAN = "boolean";
    String JAVA_LANG_BOOLEAN = "java.lang.Boolean";
    String DATE = "Date";
    String JAVA_UTIL_DATE = "java.util.Date";
    String BYTE = "byte";
    String JAVA_LANG_BYTE = "java.lang.Byte";
    String SHORT = "short";
    String JAVA_LANG_SHORT = "java.lang.Short";
    //    String JAVA_MATH_BIG_INTEGER = "java.math.BigInteger";

    /**
     *
     * @param nextRs already next (基本类型只需要使用这个)
     * @param returnType 返回类型
     * @param returnTypeFieldList 返回类型的字段列表（递归获取）
     * @param field2ColumnNameMap 字段名对应的列名Map
     * @return 值对象 : 基本类型 / 对象
     * @throws IllegalAccessException 非法操作异常
     * @throws InstantiationException 实例化异常
     */
    Object getOrmValue(ResultSet nextRs, Class<?> returnType, List<Field> returnTypeFieldList, Map<String, String> field2ColumnNameMap) throws IllegalAccessException, InstantiationException, SQLException;


    static TypeStrategy chooseStrategyByReturnType(Class<?> returnType) {
        String returnTypeName = returnType.getName();
        switch (returnTypeName) {
            case BOOLEAN:
            case JAVA_LANG_BOOLEAN: {
                return new BooleanStrategy();
            }
            case SHORT:
            case JAVA_LANG_SHORT: {
                return new ShortStrategy();
            }
            case INT:
            case JAVA_LANG_INTEGER: {
                return new IntegerStrategy();
            }
            case FLOAT:
            case JAVA_LANG_FLOAT: {
                return new FloatStrategy();
            }
            case DOUBLE:
            case JAVA_LANG_DOUBLE: {
                return new DoubleStrategy();
            }
            case LONG:
            case JAVA_LANG_LONG: {
                return new LongStrategy();
            }
            case BYTE:
            case JAVA_LANG_BYTE: {
                return new ByteStrategy();
            }
            case JAVA_LANG_STRING: {
                return new StringStrategy();
            }

            case JAVA_MATH_BIG_DECIMAL: {
                return new BigDecimalStrategy();
            }
            case DATE:
            case JAVA_UTIL_DATE: {
                return new DateStrategy();
            }
        }
        return new ObjectStrategy();
    }



}
