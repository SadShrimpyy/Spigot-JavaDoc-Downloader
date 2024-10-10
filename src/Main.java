import utils.Desktop;
import connection.Downloader;
import utils.FILES;
import connection.HTML;
import utils.JarToDir;
import connection.URLS;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        Downloader downloader = new Downloader();
        if (!downloader.downloadPage(URLS.METADATA.get(), FILES.MAVEN_METADATA_XML.get())) return;

        LinkedList<String> versions = getVersions(FILES.MAVEN_METADATA_XML.get());
        if (versions == null) return;

        JarToDir jarToDir = new JarToDir();
        String[][] mat = new String[2][versions.size()];
        AtomicInteger counter = new AtomicInteger();
        versions.forEach(snapshotVersion -> {
            downloader.downloadPage(URLS.VERSION.get().replace("%tag-version-snapshot%", snapshotVersion), snapshotVersion + ".html");
            String timestampVersion = parseVersionFromHtmlTag(snapshotVersion);
            downloader.downloadJar(composeJavadocURL(snapshotVersion, timestampVersion), timestampVersion + "-javadoc.jar");
            checkAndDelete(snapshotVersion + ".html");
            jarToDir.extract(timestampVersion + "-javadoc.jar", "javadocs\\" + timestampVersion + "-javadoc", snapshotVersion);
            checkAndDelete(timestampVersion + "-javadoc.jar");
            int i = counter.getAndIncrement();
            mat[0][i] = snapshotVersion;
            mat[1][i] = timestampVersion;
            updateHtmlComponent(snapshotVersion, timestampVersion);
        });
        updateHtmlComponent(mat, counter.get());

        Desktop desktop = new Desktop();
        desktop.openHtml();
    }

    private static void updateHtmlComponent(String snapshotVersion, String timestampVersion) {
        File indexHTML = new File(FILES.JAVADOCS_INDEX_HTML.get());
        try {
            if (!indexHTML.exists()) indexHTML.createNewFile();
            FileWriter writer = new FileWriter(indexHTML);
            writer.write(HTML.INDEX_COMPONENT.get());
            writer.append(HTML.VERSION_COMPONENT.get()
                    .replace("%tag-timestamp-version%", timestampVersion)
                    .replace("%tag-snapshot-version%", snapshotVersion));
            writer.append(HTML.FOOT_COMPONENT.get());
            writer.close();
            System.out.println("Updated index.html with new javadoc version: " + snapshotVersion);
        } catch (IOException e) {
            System.out.println("Error updating index.html with new javadoc version " + snapshotVersion + ": " + e.getMessage());
        }
    }

    private static void updateHtmlComponent(String[][] mat, int totVersions) {
        File indexHTML = new File(FILES.JAVADOCS_INDEX_HTML.get());
        try {
            if (!indexHTML.exists()) indexHTML.createNewFile();
            FileWriter writer = new FileWriter(indexHTML);
            writer.write(HTML.INDEX_COMPONENT.get());
            for (int i = totVersions - 1; i >= 0 ; i--) {
                writer.append(HTML.VERSION_COMPONENT.get()
                        .replace("%tag-timestamp-version%", mat[1][i])
                        .replace("%tag-snapshot-version%", mat[0][i]));
            }
            writer.append(HTML.FOOT_COMPONENT.get());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error updating index.html: " + e.getMessage());
        }
    }

    private static void checkAndDelete(String fileName) {
        File f = new File(fileName);
        if (f.exists()) f.delete();
    }

    private static String parseVersionFromHtmlTag(String v) {
        return getFileContent(v + ".html")
                .replaceAll("(.*)(<a href=\")(%tag-version%-\\d{8}.\\d{6}-\\d{1,3})(/\\\">)(.*)"
                                .replace("%tag-version%", v.replace("-SNAPSHOT", "")),
                        "$3");
    }

    private static String composeJavadocURL(String versionSnapshot, String versionTimestamp) {
        return URLS.JAVADOC.get().replace("%tag-version-timestamp%", versionTimestamp).replace("%tag-version-snapshot%", versionSnapshot);
    }

    private static String getFileContent(String fileName) {
        File file = new File(fileName);
        Scanner reader = null;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Error reading metadata file " + fileName + ": " + e.getMessage());
            return null;
        }
        String line = reader.nextLine();
        reader.close();
        if (file.exists()) file.delete();
        return line;
    }

    private static LinkedList<String> getVersions(String outFile) {
        File metadata = new File(outFile);
        Scanner reader = null;
        try {
            reader = new Scanner(metadata);
        } catch (FileNotFoundException e) {
            System.out.println("Error reading metadata file " + outFile + ": " + e.getMessage());
            return null;
        }

        String[] buffer = reader.nextLine().replaceAll("(.*<versions>)(.*)(</versions>.*)", "$2")
                .replace(" ", "\n")
                .replaceAll("(<version>)(.*)(</version>)", "$2")
                .split("\n");
        LinkedList<String> versions = new LinkedList<>();
        for (String s : buffer) {
            if (s.isBlank()) continue;
            versions.add(s);
        }

        reader.close();
        if (metadata.exists()) metadata.delete();
        return versions;
    }
    
}