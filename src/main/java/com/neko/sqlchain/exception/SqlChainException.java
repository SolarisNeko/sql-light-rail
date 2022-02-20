package com.neko.sqlchain.exception;

import java.util.stream.Collectors;

/**
 * @author SolarisNeko
 * @date 2022-02-20
 */
public class SqlChainException extends RuntimeException {


    public SqlChainException(String message) {
        super(message);
    }
}