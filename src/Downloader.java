import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Downloader {

    public boolean downloadPage(String connectionUrl, String outFile) {
        try {
            URL url = new URL(connectionUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
            }
            reader.close();
            writer.close();
            System.out.println("Successfully downloaded " + outFile + " from " + connectionUrl);
            return true;
        } catch (IOException e) {
            System.out.println("Error while downloading from " + connectionUrl + ": " + e.getMessage());
            return false;
        }
    }

    public void downloadJar(String fileURL, String savePath) {
        URL url = null;
        try {
            url = new URL(fileURL);
            try(InputStream in = url.openStream()) {
                Files.copy(in, Path.of(savePath), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println("Error downloading " + fileURL + ": " + e.getMessage());
        }

    }

}
