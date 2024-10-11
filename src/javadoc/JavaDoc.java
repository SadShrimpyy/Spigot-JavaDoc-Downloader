package javadoc;

import connection.NetworkUtility;
import connection.URLS;
import utils.FileHandler;
import utils.Log;

import java.io.*;
import java.util.zip.*;

public class JavaDoc {

    private final NetworkUtility networkUtility;

    public JavaDoc() {
        this.networkUtility = new NetworkUtility();
    }

    public void extractJavadoc(String jarFilePath, String outputDir, String version) {
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

    public String composeJavadocURL(String versionSnapshot, String versionTimestamp) {
        return URLS.JAVADOC.get().replace("%tag-version-timestamp%", versionTimestamp).replace("%tag-version-snapshot%", versionSnapshot);
    }

}