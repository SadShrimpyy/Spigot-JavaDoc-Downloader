package javadoc;

import cache.CacheHandler;
import cache.VTag;
import connection.HTML;
import connection.NetworkUtility;
import connection.URLS;
import utils.Desktop;
import utils.FILES;
import utils.FileHandler;
import utils.Log;

import java.io.*;
import java.util.LinkedList;
import java.util.zip.*;

public class JavaDoc {

    private static final NetworkUtility networkUtility = new NetworkUtility();

    public static void extractJavadoc(String jarFilePath, String outputDir, String version) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(jarFilePath))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                File outputFile = new File(outputDir, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    new File(outputFile.getParent()).mkdirs();
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            bos.write(buffer, 0, length);
                        }
                    }
                }
                zis.closeEntry();
            }
            Log.logInfo("Extracting " + version + "javadoc to: " + outputDir);
        } catch (IOException e) {
            Log.logError("Failed to extract " + jarFilePath + " : " + e.getMessage());
        }
    }

    public void generateJavadoc(String snapshotVersion, String timestampVersion) {
        String javadocJar = timestampVersion + "-javadoc.jar";
        String outputLocation = "javadocs\\" + timestampVersion + "-javadoc";

        Log.logInfo("Fetching javadoc's version: " + timestampVersion);
        networkUtility.fetchFileFromUrl(URLS.VERSION.get().replace("%tag-version-snapshot%", snapshotVersion), snapshotVersion + ".html");
        networkUtility.fetchJarFromUrl(composeJavadocURL(snapshotVersion, timestampVersion), timestampVersion + "-javadoc.jar");
        FileHandler.checkAndDelete(snapshotVersion + ".html");

        Log.logInfo("Extracting javadoc's " + timestampVersion + "-javadoc.jar");
        extractJavadoc(javadocJar, outputLocation, snapshotVersion);
        FileHandler.checkAndDelete(javadocJar);
    }

    public static String composeJavadocURL(String versionSnapshot, String versionTimestamp) {
        return URLS.JAVADOC.get().replace("%tag-version-timestamp%", versionTimestamp).replace("%tag-version-snapshot%", versionSnapshot);
    }

    public static void generateAllJavadoc() {
        LinkedList<String> versions = FileHandler.getVersions(FILES.MAVEN_METADATA_XML.get());
        if (versions == null)
            return;

        versions.forEach(snapshotVersion -> {
            networkUtility.fetchFileFromUrl(URLS.VERSION.get().replace("%tag-version-snapshot%", snapshotVersion), snapshotVersion + ".html");
            String timestampVersion = FileHandler.parseVersionFromHtmlTag(snapshotVersion);
            networkUtility.fetchJarFromUrl(composeJavadocURL(snapshotVersion, timestampVersion), timestampVersion + "-javadoc.jar");
            FileHandler.checkAndDelete(snapshotVersion + ".html");
            extractJavadoc(timestampVersion + "-javadoc.jar", "javadocs\\" + timestampVersion + "-javadoc", snapshotVersion);
            FileHandler.checkAndDelete(timestampVersion + "-javadoc.jar");
            CacheHandler.clearVersions(versions.size());
            CacheHandler.addSnapshotVersion(snapshotVersion);
            CacheHandler.addTimestampVersion(timestampVersion);
        });
        updateHtmlComponent(CacheHandler.getVersions(), CacheHandler.getTotalCachedVersions());
        CacheHandler.writeCacheToFile();
        createStylesheet();

        Desktop desktop = new Desktop();
        desktop.openHtml();
    }

    private static void createStylesheet() {
        File stylesheetFile = new File(FILES.JAVADOCS_STYLESHEET_CSS.get());
        try {
            if (!stylesheetFile.exists())
                stylesheetFile.createNewFile();
            FileWriter writer = new FileWriter(stylesheetFile);
            writer.write(HTML.STYLESHEET_CSS.get());
            writer.close();
            Log.logInfo("Created stylesheet.css");
        } catch (IOException e) {
            Log.logError("Failed to create " + stylesheetFile.getName() + e.getMessage());
        }
    }

    private static void updateHtmlComponent(String[][] mat, int totVersions) {
        File indexHTML = new File(FILES.JAVADOCS_INDEX_HTML.get());
        try {
            if (!indexHTML.exists())
                indexHTML.createNewFile();
            FileWriter writer = new FileWriter(indexHTML);
            writer.write(HTML.INDEX_COMPONENT.get());
            for (int i = totVersions - 1; i >= 0 ; i--) {
                writer.append(HTML.VERSION_COMPONENT.get()
                        .replace("%tag-timestamp-version%", mat[i][VTag.TIMESTAMP.get()])
                        .replace("%tag-snapshot-version%", mat[i][VTag.SNAPSHOT.get()])
                        .replace("%tag-element-list%", Integer.toString(totVersions - i)));
            }
            writer.append(HTML.FOOT_COMPONENT.get());
            writer.close();
        } catch (IOException e) {
            Log.logWarn("Failed to update " + indexHTML.getName() + ": " + e.getMessage());
        }
    }
}