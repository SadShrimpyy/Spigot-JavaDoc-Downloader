import cache.CacheHandler;
import connection.NetworkUtility;
import utils.FILES;
import javadoc.JavaDoc;
import connection.URLS;
import utils.Log;

public class Main {

    public static void main(String[] args) {
        NetworkUtility networkUtility = new NetworkUtility();
        JavaDoc javadoc = new JavaDoc();
        boolean fetchAllJavadocs = true;

        if (CacheHandler.cacheExists()) {
            fetchAllJavadocs = CacheHandler.checkCachedJavadocs();
        }

        if (fetchAllJavadocs) {
            Log.logWarn("Couldn't find cache file, fetching all javadocs");
            String log = networkUtility.fetchFileFromUrl(URLS.METADATA.get(), FILES.MAVEN_METADATA_XML.get())
                ? "Successfully downloaded " + FILES.MAVEN_METADATA_XML.get() + " from " + URLS.METADATA.get()
                : "Failed to download " + FILES.MAVEN_METADATA_XML.get() + " from " + URLS.METADATA.get();
            Log.auto(log);
        }

        javadoc.generateAllJavadoc();
    }

}