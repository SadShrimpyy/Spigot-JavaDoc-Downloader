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

    public static void auto(String... log) {
        String auto = String.join(" ", log);
        if (auto.toLowerCase().contains("failed")
                || auto.toLowerCase().contains("error")) {
            Log.logError(log);
        } else if (auto.toLowerCase().contains("warning")) {
            Log.logWarn(log);
        } else {
            Log.logInfo(log);
        }
    }
}