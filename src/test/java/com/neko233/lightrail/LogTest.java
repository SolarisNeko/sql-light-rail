package com.neko233.lightrail;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author SolarisNeko
 * Date on 2022-04-17
 */
@Slf4j
public class LogTest {


    @Test
    public void testLog() {
        try {
            exceptionMethod();
        } catch (Exception e) {
            log.error("error. sql = {}, cause = {}", "ttt", e.getCause(), e);
        }
    }

    public void exceptionMethod() {
        Integer i = null;
        i.getClass();
    }
}
