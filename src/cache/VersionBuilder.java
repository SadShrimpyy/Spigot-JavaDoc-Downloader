package cache;

public class VersionBuilder {

    private String timestamp;
    private String snapshot;
    private boolean needFetch;

    public VersionBuilder timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public VersionBuilder snapshot(String snapshot) {
        this.snapshot = snapshot;
        return this;
    }

    public VersionBuilder requiresFetch(boolean needFetch) {
        this.needFetch = needFetch;
        return this;
    }

    public Version build() {
        return new Version(timestamp, snapshot, needFetch);
    }

}
