package cache;

import connection.HTML;
import javadoc.JavaDoc;
import utils.FILES;
import utils.FileHandler;
import utils.Log;

import java.io.*;
import java.util.Scanner;

public class CacheHandler {

    private static final JavaDoc javadoc = new JavaDoc();
    private static final File cacheFile = new File(FILES.CACHE_FILE_TXT.get());
    private static String[][] versions;
    private static String[] cachedVersions;

    public CacheHandler() {
        try {
            cachedVersions = new Scanner(cacheFile)
                    .nextLine()
                    .split(";");
        } catch (FileNotFoundException e) {
            cachedVersions = null;
        }
    }

    public static void persistCacheData(String[][] mat, int totVersions) {
        try {
            if (!cacheFile.exists())
                cacheFile.createNewFile();
            FileWriter writer = new FileWriter(cacheFile);
            for (int i = 0; i < totVersions ; i++) {
                writer.append(mat[i][VTag.SNAPSHOT.get()])
                        .append(",")
                        .append(mat[i][VTag.TIMESTAMP.get()])
                        .append(";");
            }
        } catch (IOException e) {
            System.out.println("Error during the creation of " + FILES.CACHE_FILE_TXT.get() + ": " + e.getMessage());
            return;
        }
        System.out.println("Cache file created successfully!");
    }

    public static boolean checkCachedJavadocs() {
        if (cachedVersions == null) {
            Log.logWarn("Failed to read " + FILES.CACHE_FILE_TXT.get() + ", cache invalidated");
            FileHandler.checkAndDelete(FILES.CACHE_FILE_TXT.get());
            return true;
        }

        Log.logInfo("Cache file exists, reading to avoid fetch existing javadocs...");
        versions = new String[cachedVersions.length][2];
        //ENDTODO
        boolean[] exists = new boolean[cachedVersions.length];
        for (int i = 0; i < cachedVersions.length; i++) {
            versions[i][VTag.SNAPSHOT.get()] = cachedVersions[i].split(",")[VTag.SNAPSHOT.get()];
            versions[i][VTag.TIMESTAMP.get()] = cachedVersions[i].split(",")[VTag.TIMESTAMP.get()];

            Log.logInfo("Found cached version: " + versions[i][VTag.SNAPSHOT.get()] + ", check if javadocs exists...");
            exists[i] = FileHandler.exists(versions[i][VTag.TIMESTAMP.get()]);
            String log = !exists[i]
                    ? "Javadoc's version " + versions[i][VTag.TIMESTAMP.get()] + " does not exist: marked for fetch"
                    : "Javadoc's version " + versions[i][VTag.TIMESTAMP.get()] + " does exist: nothing to do";
            Log.logInfo(log);
        }
        for (int i = 0; i < exists.length; i++) {
            if (!exists[i]) {
                javadoc.generateJavadoc(versions[i][VTag.SNAPSHOT.get()], versions[i][VTag.TIMESTAMP.get()]);
                rebuildCachedHtmlComponent();
            }
        }
        return false;
    }

    private static void rebuildCachedHtmlComponent() {
        File indexHTML = new File(FILES.JAVADOCS_INDEX_HTML.get());
        try {
            if (!indexHTML.exists())
                indexHTML.createNewFile();
            FileWriter writer = new FileWriter(indexHTML);
            writer.write(HTML.INDEX_COMPONENT.get());
            for (int i = cachedVersions.length - 1; i >= 0 ; i--) {
                writer.append(HTML.VERSION_COMPONENT.get()
                        .replace("%tag-timestamp-version%", versions[i][VTag.TIMESTAMP.get()])
                        .replace("%tag-snapshot-version%", versions[i][VTag.SNAPSHOT.get()])
                        .replace("%tag-element-list%", Integer.toString(cachedVersions.length - i)));
            }
            writer.append(HTML.FOOT_COMPONENT.get());
            writer.close();
        } catch (IOException e) {
            Log.logError("Failed to update " + indexHTML.getName() + ": " + e.getMessage());
        }
    }

    public static boolean cacheExists() {
        return cacheFile.exists();
    }
}