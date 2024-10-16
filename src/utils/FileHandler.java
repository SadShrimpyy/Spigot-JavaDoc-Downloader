package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

public class FileHandler {

    public static boolean exists(String timestampVersion) {
        return new File("javadocs\\" + timestampVersion + "-javadoc").exists();
    }

    public static void checkAndDelete(String fileName) {
        checkAndDelete(new File(fileName));
    }

    public static void checkAndDelete(File f) {
        if (f.exists()) {
            if (f.delete()) {
                Log.logInfo("Deleted " + f.getName());
            } else {
                Log.logWarn("Failed to delete " + f.getName() + "!");
            }
        }
    }

    public static String parseVersionFromHtmlTag(String v) {
        return Objects.requireNonNull(getFileContent(v + ".html"))
                .replaceAll("(.*)(<a href=\")(%tag-version%-\\d{8}.\\d{6}-\\d{1,3})(/\\\">)(.*)"
                                .replace("%tag-version%", v.replace("-SNAPSHOT", "")),
                        "$3");
    }

    public static String getFileContent(String fileName) {
        File file = new File(fileName);
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            Log.logError("Failed to read metadata file " + fileName + ": " + e.getMessage());
            return null;
        }
        String line = reader.nextLine();
        reader.close();
        checkAndDelete(file);
        return line;
    }

    public static LinkedList<String> getVersions(String outFile) {
        File metadata = new File(outFile);
        Scanner reader;
        try {
            reader = new Scanner(metadata);
        } catch (FileNotFoundException e) {
            Log.logError("Failed to read metadata file " + outFile + ": " + e.getMessage());
            return null;
        }

        String[] buffer = reader.nextLine().replaceAll("(.*<versions>)(.*)(</versions>.*)", "$2")
                .replace(" ", "\n")
                .replaceAll("(<version>)(.*)(</version>)", "$2")
                .split("\n");
        LinkedList<String> versions = new LinkedList<>();
        for (String s : buffer) {
            if (s.isBlank()) {
                continue;
            }
            versions.add(s);
        }

        reader.close();
        return versions;
    }

}