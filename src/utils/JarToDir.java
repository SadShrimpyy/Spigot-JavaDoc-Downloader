package utils;

import java.io.*;
import java.util.zip.*;

public class JarToDir {

    public void extract(String jarFilePath, String outputDir, String version) {
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
            System.out.println("Extracting " + version + "javadoc to: " + outputDir);
        } catch (IOException e) {
            System.out.println("Error while extracting " + jarFilePath + " : " + e.getMessage());
        }
    }
}