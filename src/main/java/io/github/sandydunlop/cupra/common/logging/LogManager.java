package io.github.sandydunlop.cupra.common.logging;

import java.util.HashMap;
import java.util.Map;


public class LogManager {
    private static Map<String, Logger> map = new HashMap<>();

    private LogManager(){
        // Hide the public constructor
    }

    public static Logger getLogger(String name) {
        Logger logger = null;
        if (map.containsKey(name)){
            return map.get(name);
        }else{
            logger = new Logger(name);
            map.put(name, logger);
        }
        return logger;
    }
}
