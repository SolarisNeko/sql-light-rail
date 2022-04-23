package com.neko233.lightrail.orm;

import com.neko233.lightrail.strategy.TypeStrategy;
import com.neko233.lightrail.util.CamelCaseUtil;
import com.neko233.lightrail.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * @author SolarisNeko
 * Date on 2022-02-26
 */
@Slf4j
public class RailPlatformOrm {

    /**
     * ORM convert
     *
     * @param rs         SQL 结果集
     * @param returnType 要生成的 object 的 Class
     * @param <T>        范型
     * @return SQL ResultSet 通过 ORM 映射后的 Java DataList
     */
    public static <T> List<T> orm(ResultSet rs, Class<?> returnType) {
        List<Field> returnTypeFieldList = ReflectUtil.getAllFields(returnType);
        Map<String, String> fieldColumnMap = returnTypeFieldList.stream()
            .collect(toMap(
                Field::getName,
                field -> CamelCaseUtil.getBigCamelLowerName(field.getName()),
                (t1, t2) -> t1)
            );

        List<T> dataList = new ArrayList<>();
        TypeStrategy strategy = TypeStrategy.chooseStrategyByReturnType(returnType);
        try {
            while (rs.next()) {
                T newObject = (T) strategy.getOrmValue(rs, returnType, returnTypeFieldList, fieldColumnMap);
                dataList.add(newObject);
            }
        } catch (Exception e) {
            log.error("[RailPlatformOrm] ORM Mapping error!", e);
            return dataList;
        }

        return dataList;
    }


}
