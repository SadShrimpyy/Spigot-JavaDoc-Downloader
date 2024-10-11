import cache.CacheHandler;
import cache.VTag;
import utils.Desktop;
import connection.NetworkUtility;
import utils.FILES;
import connection.HTML;
import javadoc.JavaDoc;
import connection.URLS;
import utils.FileHandler;
import utils.Log;

import java.io.*;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        NetworkUtility networkUtility = new NetworkUtility();
        JavaDoc javadoc = new JavaDoc();
        boolean fetchJavadocs = true;

        if (CacheHandler.exists()) {
            fetchJavadocs = CacheHandler.fetchFromFile();
        }

        if (fetchJavadocs) {
            Log.logError("Cache file didn't exists, fetching all javadocs");
            if (!networkUtility.fetchFileFromUrl(URLS.METADATA.get(), FILES.MAVEN_METADATA_XML.get())) {
                return;
            }
        }

        //TODO: From here is all wrong
        LinkedList<String> versions = FileHandler.getVersions(FILES.MAVEN_METADATA_XML.get());
        if (versions == null) return;

        JavaDoc jarToDir = new JavaDoc();
        String[][] mat = new String[2][versions.size()];
        AtomicInteger counter = new AtomicInteger();

        versions.forEach(snapshotVersion -> {
            int i = counter.getAndIncrement();
            networkUtility.fetchFileFromUrl(URLS.VERSION.get().replace("%tag-version-snapshot%", snapshotVersion), snapshotVersion + ".html");
            String timestampVersion = FileHandler.parseVersionFromHtmlTag(snapshotVersion);
            networkUtility.fetchJarFromUrl(javadoc.composeJavadocURL(snapshotVersion, timestampVersion), timestampVersion + "-javadoc.jar");
            FileHandler.checkAndDelete(snapshotVersion + ".html");
            jarToDir.extractJavadoc(timestampVersion + "-javadoc.jar", "javadocs\\" + timestampVersion + "-javadoc", snapshotVersion);
            FileHandler.checkAndDelete(timestampVersion + "-javadoc.jar");
            mat[i][VTag.SNAPSHOT.get()] = snapshotVersion;
            mat[i][VTag.TIMESTAMP.get()] = timestampVersion;
            updateHtmlComponent(snapshotVersion, timestampVersion);
        });
        // TODO: Get mat from CacheHandler
        updateHtmlComponent(mat, counter.get());
        CacheHandler.persistCacheData(mat, counter.get());
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

    private static void updateHtmlComponent(String snapshotVersion, String timestampVersion) {
        File indexHTML = new File(FILES.JAVADOCS_INDEX_HTML.get());
        try {
            if (!indexHTML.exists())
                indexHTML.createNewFile();
            FileWriter writer = new FileWriter(indexHTML);
            writer.write(HTML.INDEX_COMPONENT.get());
            writer.append(HTML.VERSION_COMPONENT.get()
                    .replace("%tag-timestamp-version%", timestampVersion)
                    .replace("%tag-snapshot-version%", snapshotVersion));
            writer.append(HTML.FOOT_COMPONENT.get());
            writer.close();
            Log.logInfo("Updated " + indexHTML.getName() + " with new javadoc version: " + snapshotVersion);
        } catch (IOException e) {
            Log.logError("Failed to update " + indexHTML.getName() + " with new javadoc version " + snapshotVersion + ": " + e.getMessage());
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
            Log.logError("Failed to update " + indexHTML.getName() + ": " + e.getMessage());
        }
    }

}