package utils;

public class Log {

    public static void logInfo(String... log) {
        System.out.println("[INFO] " + String.join(" ", log));
    }

    public static void logWarn(String... log) {
        System.out.println("[WARN] " + String.join(" ", log));
    }

    public static void logError(String... log) {
        System.out.println("[ERROR] " + String.join(" ", log));
    }

}