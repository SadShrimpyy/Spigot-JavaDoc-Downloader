package cache;

public class Version {

    private final String timestamp;
    private final String snapshot;
    private final boolean needFetch;

    public Version(String timestamp, String snapshot, boolean needFetch) {
        this.timestamp = timestamp;
        this.snapshot = snapshot;
        this.needFetch = needFetch;
    }

    public String getTimestampStr() {
        return timestamp;
    }

    public String getSnapshotStr() {
        return snapshot;
    }

    public boolean requiresFetch() {
        return needFetch;
    }

}