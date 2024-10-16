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
        boolean fetchAllJavadocs = true;

        if (CacheHandler.cacheExists()) {
            fetchAllJavadocs = CacheHandler.checkCachedJavadocs();
        }

        if (fetchAllJavadocs) {
            Log.logWarn("Couldn't find cache file, fetching all javadocs");
            String log = NetworkUtility.fetchFileFromUrl(URLS.METADATA.get(), FILES.MAVEN_METADATA_XML.get())
                ? "Successfully downloaded " + FILES.MAVEN_METADATA_XML.get() + " from " + URLS.METADATA.get()
                : "Failed to download " + FILES.MAVEN_METADATA_XML.get() + " from " + URLS.METADATA.get();
            Log.auto(log);
        }

        JavaDoc.generateAllJavadoc();
    }

}