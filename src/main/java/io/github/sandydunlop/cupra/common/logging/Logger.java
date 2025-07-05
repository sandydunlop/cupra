package io.github.sandydunlop.cupra.common.logging;

import io.github.sandydunlop.cupra.platform.PlatformServices;


// This Logger allos switching between info and debug logging without
// involving Minecraft's own logging settings.

public class Logger {
	private org.apache.logging.log4j.Logger logger4j;

    public Logger(String name) {
        logger4j = org.apache.logging.log4j.LogManager.getLogger(name);
    }


    public void error (String message) {
        logger4j.info(message);
    } 

    
    public void error (String message, Object... params) {
        logger4j.info(message, params);
    }

    
    public void info (String message) {
        logger4j.info(message);
    } 

    
    public void info (String message, Object... params) {
        logger4j.info(message, params);
    }

    
    public void debug (String message) {
        if (PlatformServices.getInstance().isDebugLogging()) {
            logger4j.info(message);
        }
    } 

    
    public void debug (String message, Object... params) {
        if (PlatformServices.getInstance().isDebugLogging()) {
            logger4j.info(message, params);
        }
    }
}
