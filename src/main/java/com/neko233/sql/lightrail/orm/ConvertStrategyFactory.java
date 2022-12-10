package com.neko233.sql.lightrail.orm;

import java.math.BigDecimal;
import java.util.Date;

public interface ConvertStrategyFactory {

    static ConvertStrategy chooseStrategyByReturnType(Class<?> returnType) {
        if (returnType == null) {
            throw new RuntimeException("your return type is null!");
        }
        // full path
        String returnTypeName = returnType.getName();
        if (returnTypeName.equals("boolean") || returnTypeName.equals(Boolean.class.getName())) {
            return new BooleanStrategy();
        }
        if (returnTypeName.equals("short") || returnTypeName.equals(Short.class.getName())) {
            return new ShortStrategy();
        }
        if (returnTypeName.equals("int") || returnTypeName.equals(Integer.class.getName())) {
            return new IntegerStrategy();
        }
        if (returnTypeName.equals("float") || returnTypeName.equals(Float.class.getName())) {
            return new FloatStrategy();
        }
        if (returnTypeName.equals("double") || returnTypeName.equals(Double.class.getName())) {
            return new DoubleStrategy();
        }
        if (returnTypeName.equals("long") || returnTypeName.equals(Long.class.getName())) {
            return new LongStrategy();
        }
        if (returnTypeName.equals("byte") || returnTypeName.equals(Byte.class.getName())) {
            return new ByteStrategy();
        }
        if (returnTypeName.equals(String.class.getName())) {
            return new StringStrategy();
        }
        if (returnTypeName.equals(BigDecimal.class.getName())) {
            return new BigDecimalStrategy();
        }
        if (returnTypeName.equals(Date.class.getName())) {
            return new DateStrategy();
        }
        return new ObjectStrategy();
    }


}
