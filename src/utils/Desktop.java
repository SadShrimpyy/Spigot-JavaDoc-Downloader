package utils;

import java.io.File;
import java.io.IOException;

public class Desktop {

    public void openHtml() {
        try {
            File indexHTML = new File(FILES.JAVADOCS_INDEX_HTML.get());
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(indexHTML);
            } else {
                System.out.println("Desktop is not supported on this platform.");
            }
        } catch (IOException e) {
            System.out.println("Error opening index.html: " + e.getMessage());
        }
    }

}