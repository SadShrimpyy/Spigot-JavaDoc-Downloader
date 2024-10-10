package connection;

public enum HTML {
    INDEX_COMPONENT("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\" \"http://www.w3.org/TR/html4/frameset.dtd\"> <html lang=\"en\"> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> <title>Spigot-API JavaDocs</title> <link rel=\"stylesheet\" href=\"stylesheet.css\"> </head> <main> <div class=\"box\"> <h2>Spigot's documentation:</h2> <ul>"),
    VERSION_COMPONENT("<li><span>%tag-element-list%</span><a href=\"%tag-timestamp-version%-javadoc/index.html\">%tag-snapshot-version%</a></li>"),
    FOOT_COMPONENT("</ul> </div> </main> </html>"),
    STYLESHEET_CSS(" * { margin: 0; padding: 0; box-sizing: border-box; font-family: sans-serif; }   body { margin: 5%; display: flex; justify-content: center; align-items: center; min-height: 100vh; background: #f6f6f6; transition-duration: 0.4s; }   .box { width: 300px; border-bottom: 20px solid #bb7a2a; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px; transition-duration: 0.2s; }   .box h2 { color: #fff; background: #bb7a2a; padding: 10px 20px; font-size: 20px; font-weight: 700; border-top-left-radius: 10px; border-top-right-radius: 10px; transition-duration: 0.4s; }   .box ul { position: relative; background: #fff; transition-duration: 0.4s; }   .box ul:hover li { opacity: 0.25; transition-duration: 0.4s; }   .box ul li { list-style: none; padding: 10px; width: 100%; background: #fff; box-shadow: 0 5px 25px rgba(0, 0, 0, 0.1); transition: transform 0.8s; }   .box ul li:hover { transform: scale(1.1); z-index: 5; background: #bb7a2a; box-shadow: 0 5px 25px rgba(0, 0, 0, 0.2); color: #fff; opacity: 1; transition-duration: 0.4s; }   .box ul li span { width: 20px; height: 20px; text-align: center; line-height: 20px; background: #bb7a2a; color: #fff; display: inline-block; border-radius: 50%; margin-right: 10px; font-size: 12px; font-weight: 600; transform: translateY(-2px); transition-duration: 0.4s; }   .box ul li:hover span { background: #fff; color: #bb7a2a; transition-duration: 0.4s; }   a:link, a:visited, a:active { text-decoration: none; color: #4c6b87; transition-duration: 0.4s; }   a:hover, a:focus  { text-decoration: none; color: #ffffff; transition-duration: 0.4s; }"),
    ;

    private final String url;

    HTML(String url) {
        this.url = url;
    }

    public String get() {
        return this.url;
    }
}