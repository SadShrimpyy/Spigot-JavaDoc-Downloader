package cache;

import connection.HTML;
import javadoc.JavaDoc;
import utils.FILES;
import utils.FileHandler;
import utils.Log;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class CacheHandler {

    private static final JavaDoc javadoc = new JavaDoc();
    private static final File cacheFile = new File(FILES.CACHE_FILE_TXT.get());
    private static String[][] versions;
    private static int totalCachedVersions;
    private static HashMap<String, Boolean> needsJavadocFetch;

    public static void writeCacheToFile() {
        try {
            if (!cacheFile.exists())
                cacheFile.createNewFile();
            FileWriter writer = new FileWriter(cacheFile);
            for (int i = 0; i < totalCachedVersions ; i++) {
                writer.append(versions[i][VTag.SNAPSHOT.get()])
                        .append(",")
                        .append(versions[i][VTag.TIMESTAMP.get()])
                        .append(";");
            }
            writer.close();
        } catch (IOException e) {
            Log.logWarn("Error during the creation of " + FILES.CACHE_FILE_TXT.get() + ": " + e.getMessage());
            return;
        }
        Log.logInfo("Cache file created successfully!");
    }

    public static boolean checkCachedJavadocs() {
        String[] scannedVersions;
        try {
            scannedVersions = new Scanner(cacheFile)
                    .nextLine()
                    .split(";");
            if (Arrays.toString(scannedVersions).trim().isEmpty())
                scannedVersions = null;
        } catch (FileNotFoundException e) {
            scannedVersions = null;
        }
        if (scannedVersions == null) {
            Log.logWarn("Failed to read " + FILES.CACHE_FILE_TXT.get() + ", cache invalidated");
            FileHandler.checkAndDelete(FILES.CACHE_FILE_TXT.get());
            return true;
        }

        Log.logInfo("Cache file exists, reading to avoid fetch existing javadocs...");
        versions = new String[scannedVersions.length][2];
        needsJavadocFetch = new HashMap<>(scannedVersions.length);
        for (int i = 0; i < scannedVersions.length; i++) {
            versions[i][VTag.SNAPSHOT.get()] = scannedVersions[i].split(",")[VTag.SNAPSHOT.get()];
            versions[i][VTag.TIMESTAMP.get()] = scannedVersions[i].split(",")[VTag.TIMESTAMP.get()];
            Log.logInfo("Found cached version: " + versions[i][VTag.SNAPSHOT.get()] + ", check if javadocs exists...");

            boolean needFetch = !FileHandler.exists(versions[i][VTag.TIMESTAMP.get()]);
            needsJavadocFetch.put(versions[i][VTag.SNAPSHOT.get()], needFetch);
            String log = !needFetch
                    ? "Javadoc's version " + versions[i][VTag.TIMESTAMP.get()] + " does not exist: marked for fetch"
                    : "Javadoc's version " + versions[i][VTag.TIMESTAMP.get()] + " does exist: nothing to do";
            Log.logInfo(log);
        }
        for (int i = 0; i < needsJavadocFetch.size(); i++) {
            if (needsJavadocFetch.get(versions[i][VTag.SNAPSHOT.get()])) {
                javadoc.generateJavadoc(versions[i][VTag.SNAPSHOT.get()], versions[i][VTag.TIMESTAMP.get()]);
                rebuildCachedHtmlComponent(scannedVersions);
            }
        }
        return false;
    }

    private static void rebuildCachedHtmlComponent(String[] scannedVersions) {
        File indexHTML = new File(FILES.JAVADOCS_INDEX_HTML.get());
        try {
            if (!indexHTML.exists())
                indexHTML.createNewFile();
            FileWriter writer = new FileWriter(indexHTML);
            writer.write(HTML.INDEX_COMPONENT.get());
            for (int i = scannedVersions.length - 1; i >= 0 ; i--) {
                writer.append(HTML.VERSION_COMPONENT.get()
                        .replace("%tag-timestamp-version%", versions[i][VTag.TIMESTAMP.get()])
                        .replace("%tag-snapshot-version%", versions[i][VTag.SNAPSHOT.get()])
                        .replace("%tag-element-list%", Integer.toString(scannedVersions.length - i)));
            }
            writer.append(HTML.FOOT_COMPONENT.get());
            writer.close();
        } catch (IOException e) {
            Log.logWarn("Failed to update " + indexHTML.getName() + ": " + e.getMessage());
        }
    }

    public static boolean shouldFetchJavadoc(String timestampVersion) {
        if (needsJavadocFetch.containsKey(timestampVersion)) {
            return needsJavadocFetch.get(timestampVersion);
        }
        return false;
    }

    public static boolean cacheExists() {
        return cacheFile.exists();
    }

    public static void clearVersions(int metadataVersions) {
        versions = new String[metadataVersions][2];
        totalCachedVersions = 0;
    }

    public static void addSnapshotVersion(String snapshotVersion) {
        versions[totalCachedVersions][VTag.SNAPSHOT.get()] = snapshotVersion;
    }

    public static void addTimestampVersion(String timestampVersion) {
        versions[totalCachedVersions][VTag.TIMESTAMP.get()] = timestampVersion;
    }

    public static void incrementTotalCachedVersions() {
        totalCachedVersions++;;
    }

    public static String[][] getVersions() {
        return versions;
    }

    public static int getTotalCachedVersions() {
        return totalCachedVersions;
    }
}