package main.java;

public class Logger {

    private static int level;
    private static Logger instance;

    private Logger() {
        level = 1;
    }

    public static void log(String message, int level) {
        if (level <= Logger.level) {
            System.out.println(message);
        }
    }

    public static void setLevel(int level) {
        Logger.level = level;
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public static final int MINIMAL = 0;
    public static final int INFO = 1;
    public static final int DEBUG = 2;
}
