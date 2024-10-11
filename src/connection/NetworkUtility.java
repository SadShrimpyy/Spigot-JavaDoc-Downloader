package connection;

import utils.Log;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class NetworkUtility {

    public boolean fetchFileFromUrl(String remoteUrl, String targetFile) {
        try {
            URL url = new URL(remoteUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
            }
            reader.close();
            writer.close();
            Log.logInfo("Successfully downloaded " + targetFile + " from " + remoteUrl);
            return true;
        } catch (IOException e) {
            Log.logError(" Failed to download " + targetFile + " from " + remoteUrl + ": " + e.getMessage());
            return false;
        }
    }

    public void fetchJarFromUrl(String remoteUrl, String downloadJar) {
        URL url;
        try {
            url = new URL(remoteUrl);
            try(InputStream in = url.openStream()) {
                Files.copy(in, Path.of(downloadJar), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            Log.logError(" Failed to download " + downloadJar + " from " + remoteUrl + ": " + e.getMessage());
        }
    }

}
