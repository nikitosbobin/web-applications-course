package server;

public enum HttpMethod {
    GET,
    POST,
    HEAD,
    CONNECT,
    PUT,
    DELETE,
    TRACE,
    OPTIONS;

    public static HttpMethod parseOrNullValue(String value) {
        try {
            return HttpMethod.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Can not parse http method: " + value);
        }
    }
}
