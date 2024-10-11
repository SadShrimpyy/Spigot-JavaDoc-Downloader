package cache;

public enum VTag {

    SNAPSHOT(0),
    TIMESTAMP(1),
    ;

    private final int version;

    VTag(int version) {
        this.version = version;
    }

    public int get() {
        return this.version;
    }

}