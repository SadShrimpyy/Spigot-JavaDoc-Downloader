import cache.CacheHandler;
import connection.NetworkUtility;
import utils.FILES;
import javadoc.JavaDoc;
import connection.URLS;
import utils.Log;

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

        // TODO: Populate the CacheHandler.needsJavadocFetch with all the missing versions and mark them as neededFetch
        //       and remove the null check in CacheHandler.requiresJavadocFetch
        JavaDoc.generateAllJavadoc();
    }

}