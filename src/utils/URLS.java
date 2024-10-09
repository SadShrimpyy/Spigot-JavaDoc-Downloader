package utils;

public enum URLS {
    METADATA("https://hub.spigotmc.org/nexus/repository/public/org/spigotmc/spigot-api/maven-metadata.xml"),
    VERSION("https://hub.spigotmc.org/nexus/service/rest/repository/browse/snapshots/org/spigotmc/spigot-api/%tag-version-snapshot%/"),
    JAVADOC("https://hub.spigotmc.org/nexus/repository/snapshots/org/spigotmc/spigot-api/%tag-version-snapshot%/spigot-api-%tag-version-timestamp%-javadoc.jar"),
    ;

    private final String url;

    URLS(String url) {
        this.url = url;
    }

    public String get() {
        return this.url;
    }
}