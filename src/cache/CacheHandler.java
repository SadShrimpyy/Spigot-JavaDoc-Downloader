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
import java.util.concurrent.atomic.AtomicInteger;

// TODO: I'd rather prefer to have a static field to be accessed than a static class!
public class CacheHandler {

    private static final JavaDoc javadoc = new JavaDoc();
    private static final File cacheFile = new File(FILES.CACHE_FILE_TXT.get());
    private static String[][] versionsMatrix; // TODO: REMOVE
    private static int totalCachedVersions; // TODO: REMOVE
    private static HashMap<String, CacheVersion> needsJavadocFetch;

    static {
        needsJavadocFetch = new HashMap<>();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void writeCacheToFile() {
        try {
            if (!cacheFile.exists()) {
                cacheFile.createNewFile();
            }
            FileWriter writer = new FileWriter(cacheFile);
            for (int i = 0; i < totalCachedVersions ; i++) {
                writer.append(versionsMatrix[i][VTag.SNAPSHOT.get()])
                        .append(",")
                        .append(versionsMatrix[i][VTag.TIMESTAMP.get()])
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
        versionsMatrix = new String[scannedVersions.length][2];
        needsJavadocFetch = new HashMap<>(scannedVersions.length);
        for (int i = 0; i < scannedVersions.length; i++) {
            versionsMatrix[i][VTag.SNAPSHOT.get()] = scannedVersions[i].split(",")[VTag.SNAPSHOT.get()];
            versionsMatrix[i][VTag.TIMESTAMP.get()] = scannedVersions[i].split(",")[VTag.TIMESTAMP.get()];
            String sp = versionsMatrix[i][VTag.SNAPSHOT.get()];
            String ts = versionsMatrix[i][VTag.TIMESTAMP.get()];
            Log.logInfo("Found cached version: " + sp + ", check if javadocs exists...");

            boolean needFetch = !FileHandler.exists(versionsMatrix[i][VTag.TIMESTAMP.get()]);
            needsJavadocFetch.put(sp, new CacheVersion(ts, sp, needFetch));
            String log = !needFetch
                    ? "Javadoc's version " + versionsMatrix[i][VTag.TIMESTAMP.get()] + " does not exist: marked for fetch"
                    : "Javadoc's version " + versionsMatrix[i][VTag.TIMESTAMP.get()] + " does exist: nothing to do";
            Log.logInfo(log);
        }

        needsJavadocFetch.forEach((key, cacheVersion) -> {
            if (cacheVersion.requiresFetch()) {
                javadoc.generateJavadoc(cacheVersion.getSnapshotVersion(), cacheVersion.getTimestampVersion());
            }
        });
        rebuildCachedHtmlComponent(needsJavadocFetch.keySet().toArray(new String[0]));
        return false;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void rebuildCachedHtmlComponent(String[] snapshots) {
        File indexHTML = new File(FILES.JAVADOCS_INDEX_HTML.get());
        try {
            if (!indexHTML.exists()) {
                indexHTML.createNewFile();
            }
            FileWriter writer = new FileWriter(indexHTML);
            writer.write(HTML.INDEX_COMPONENT.get());
            AtomicInteger desc = new AtomicInteger(needsJavadocFetch.size());
            needsJavadocFetch.forEach((key, cacheVersion) -> {
                try {
                    writer.append(HTML.VERSION_COMPONENT.get()
                            .replace("%tag-timestamp-version%", cacheVersion.getTimestampVersion())
                            .replace("%tag-snapshot-version%", cacheVersion.getSnapshotVersion())
                            .replace("%tag-element-list%", Integer.toString(snapshots.length - desc.getAndDecrement())));
                } catch (IOException e) {
                    Log.logWarn("Failed to update " + indexHTML.getName() + ": " + e.getMessage());
                }
            });
            writer.append(HTML.FOOT_COMPONENT.get());
            writer.close();
        } catch (IOException e) {
            Log.logWarn("Failed to update " + indexHTML.getName() + ": " + e.getMessage());
        }
    }

    public static boolean requiresJavadocFetch(String timestampVersion) {
        if (needsJavadocFetch.containsKey(timestampVersion)) {
            return needsJavadocFetch.get(timestampVersion).requiresFetch();
        }
        return false;
    }

    public static boolean cacheExists() {
        return cacheFile.exists();
    }

    public static void clearVersions(int metadataVersions) {
        versionsMatrix = new String[metadataVersions][2];
        totalCachedVersions = 0;
    }

    public static void addSnapshotVersion(String snapshotVersion) {
        versionsMatrix[totalCachedVersions][VTag.SNAPSHOT.get()] = snapshotVersion;
    }

    public static void addTimestampVersion(String timestampVersion) {
        versionsMatrix[totalCachedVersions][VTag.TIMESTAMP.get()] = timestampVersion;
    }

    public static void incrementTotalCachedVersions() {
        totalCachedVersions++;
    }

    public static String[][] getVersionsMatrix() {
        return versionsMatrix;
    }

    public static int getTotalCachedVersions() {
        return totalCachedVersions;
    }
}