package model;

import java.io.IOException;
import java.util.logging.*;

public class LogConfigurator {
    private final static Logger LOGS = Logger.getGlobal();

    /**
     * Set up the logger such that all log above INFO level will be recorded to log.txt
     * and only SEVERE and WARNING levels will be displayed to the console.
     */
    public static void setupLogger(){
        LogManager.getLogManager().reset();
        LOGS.setLevel(Level.ALL);

        ConsoleHandler handler1 = new ConsoleHandler();
        handler1.setLevel(Level.WARNING);
        LOGS.addHandler(handler1);

        try {
            FileHandler handler2 = new FileHandler("src/log.txt", true);
            handler2.setLevel(Level.INFO);
            handler2.setFormatter(new SimpleFormatter());
            LOGS.addHandler(handler2);
        }catch (IOException e){
            LOGS.log(Level.SEVERE, "Logger write error", e);
        }
    }

}
