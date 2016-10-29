package server;

public enum MimeType {
    css("text/css"),
    html("text/html; charset=utf-8"),
    js("text/javascript; charset=utf-8"),
    json("application/json"),
    map("application/json"),
    ico("image/vnd.microsoft.icon");

    private String value;

    MimeType(String value) {
        this.value = value;
    }

    String getValue(){
        return value;
    }
}
