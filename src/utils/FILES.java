package utils;

public enum FILES {
    MAVEN_METADATA_XML("maven-metadata.xml"),
    JAVADOCS_INDEX_HTML("javadocs\\index.html"),
    JAVADOCS_STYLESHEET_CSS("javadocs\\stylesheet.css"),
    ;

    private final String url;

    FILES(String url) {
        this.url = url;
    }

    public String get() {
        return this.url;
    }
}