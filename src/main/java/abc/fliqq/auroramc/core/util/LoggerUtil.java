package abc.fliqq.auroramc.core.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import abc.fliqq.auroramc.AuroraAPI;

public class LoggerUtil {
    private static Logger logger;

    private static void initLogger(){
        if(logger==null){
            logger= AuroraAPI.getInstance().getLogger();
        }
    }

    public static void info(String message){
        initLogger();
        logger.info(message);
    }
    public static void warning(String message){
        initLogger();
        logger.warning(message);
    }
    public static void severe(String message){
        initLogger();
        logger.severe(message);
    }
    public static void debug(String message){
        initLogger();
        logger.info("[DEBUG] " + message);
    }
    public static void severe(String message, Throwable throwable) {
        initLogger();
        logger.log(Level.SEVERE, message, throwable);
    }

    
}
