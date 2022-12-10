package com.neko233.sql.lightrail.orm;



import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author SolarisNeko
 * @date 4/23/2022
 */
public interface ConvertStrategy {

    /**
     * 自动匹配机制
     *
     * @param thisRowRs          本次扫描的 ResultSet 行
     * @param columnName         列名 @Nnullable | when null, must ObjectStrategy range scan
     * @param returnType         返回类型 @Nullable
     * @param columName2FieldMap 列名 : Object.Field @Nullable
     * @return 基本类型 / 复合类型
     * @throws SQLException           SQL 执行异常
     * @throws IllegalAccessException 非法访问
     * @throws InstantiationException 实例化异常
     */
    default Object autoMatch(ResultSet thisRowRs,
                             String columnName,
                             Class<?> returnType,
                             Map<String, Field> columName2FieldMap) throws SQLException, IllegalAccessException, InstantiationException {
        // object ORM
        if (MapUtils.isNotEmpty(columName2FieldMap)) {
            return objectConvert(thisRowRs, null, returnType, columName2FieldMap);
        }

        // single column
        if (StringUtils.isBlank(columnName) && MapUtils.isEmpty(columName2FieldMap)) {
            return singleColumn(thisRowRs);
        }

        // columnName convert
        return columnNameConvert(thisRowRs, columnName);

    }

    /**
     * @param thisRowRs already next (基本类型只需要使用这个)
     * @return 值对象 : 基本类型 / 对象
     * @throws IllegalAccessException 非法操作异常
     * @throws InstantiationException 实例化异常
     */
    Object singleColumn(final ResultSet thisRowRs) throws IllegalAccessException, InstantiationException, SQLException;


    /**
     * @param thisRowRs  already next (基本类型只需要使用这个)
     * @param columnName 列名
     * @return 值对象 : 基本类型 / 对象
     * @throws IllegalAccessException 非法操作异常
     * @throws InstantiationException 实例化异常
     */
    Object columnNameConvert(ResultSet thisRowRs, String columnName) throws SQLException, IllegalAccessException, InstantiationException;

    /**
     * default null
     */
    default Object objectConvert(ResultSet thisRowRs, String columnName, Class<?> returnType, Map<String, Field> haveAnnoFields) throws SQLException, IllegalAccessException, InstantiationException {
        return null;
    }


}
