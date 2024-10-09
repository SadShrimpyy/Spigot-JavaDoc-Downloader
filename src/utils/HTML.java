package utils;

public enum HTML {
    INDEX_COMPONENT("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\" \"http://www.w3.org/TR/html4/frameset.dtd\"> <html lang=\"en\"> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> <title>Spigot-API JavaDocs</title> <link rel=\"stylesheet\" href=\"stylesheet.css\"> <h1>Documentation Versions</h1> <p>Select a version of the documentation:</p> </head> <main> <ul>"),
    VERSION_COMPONENT("<li><a href=\"%tag-timestamp-version%-javadoc/index.html\">%tag-snapshot-version%</a></li>"),
    FOOT_COMPONENT("</ul> </main> </html>"),
    JAVADOCS_INDEX_HTML("javadocs\\index.html"),
    ;

    private final String url;

    HTML(String url) {
        this.url = url;
    }

    public String get() {
        return this.url;
    }
}