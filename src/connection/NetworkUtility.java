package connection;

import utils.Log;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class NetworkUtility {

    public static boolean fetchFileFromUrl(String remoteUrl, String targetFile) {
        try {
            URL url = new URL(remoteUrl);
            downloadFileFromUrl(targetFile, url);
            return true;
        } catch (IOException e) {
            Log.logWarn("Failed to download file " + targetFile + " from " + remoteUrl + ": " + e.getMessage());
            return false;
        }
    }

    public boolean fetchJarFromUrl(String remoteUrl, String downloadJar) {
        URL url;
        try {
            url = new URL(remoteUrl);
            downloadFileFromUrl(downloadJar, url);
        } catch (Exception e) {
            Log.logWarn("Failed to download jar " + downloadJar + " from " + remoteUrl + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    private static void downloadFileFromUrl(String targetFile, URL url) throws IOException {
        try (InputStream in = url.openStream()) {
            Files.copy(in, Path.of(targetFile), StandardCopyOption.REPLACE_EXISTING);
        }
    }

}
