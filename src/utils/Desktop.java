package utils;

import java.io.File;
import java.io.IOException;

import static java.awt.Desktop.getDesktop;
import static java.awt.Desktop.isDesktopSupported;

public class Desktop {

    public void openHtml() {
        File indexHTML = new File(FILES.JAVADOCS_INDEX_HTML.get());
        try {
            if (isDesktopSupported()) {
                Log.logInfo("Enjoy your local documentation! Opening default browser...");
                getDesktop().open(indexHTML);
            } else {
                Log.logError("Failed to open " + indexHTML.getName() + ": desktop is not supported on this platform.");
            }
        } catch (IOException e) {
            Log.logInfo("Failed to open " + indexHTML.getName() + ": " + e.getMessage());
        }
    }

}