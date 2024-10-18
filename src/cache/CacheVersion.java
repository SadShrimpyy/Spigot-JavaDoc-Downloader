package cache;

public class CacheVersion {

    private final String timestampVersion;
    private final String snapshotVersion;
    private final boolean needFetch;

    public CacheVersion(String timestampVersion, String snapshotVersion, boolean needFetch) {
        this.timestampVersion = timestampVersion;
        this.snapshotVersion = snapshotVersion;
        this.needFetch = needFetch;
    }

    public String getTimestampVersion() {
        return timestampVersion;
    }

    public String getSnapshotVersion() {
        return snapshotVersion;
    }

    public boolean requiresFetch() {
        return needFetch;
    }
}