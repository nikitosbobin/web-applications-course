package server;

import core.ILog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
    private HttpMethod method;
    private String body;
    private String queryString;
    private HashMap<String, String> headers;
    private ILog log;

    public Request(InputStream requestStream, ILog log){
        this.log = log;
        try {
            internalParse(requestStream);
        } catch (IOException e) {
            log.error(String.format("Request parsing ends with error: %s", e.getMessage()));
        }
    }

    private void internalParse(InputStream requestStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(requestStream));
        headers = readHeaders(bufferedReader);
        body = readBody(bufferedReader, getContentLength());
    }

    private HashMap<String, String> readHeaders(BufferedReader bufferedReader) throws IOException {
        HashMap<String, String> result = new HashMap<>();
        Pattern pattern = Pattern.compile("^(.*): (.*)$");
        boolean isFirstLine = true;
        String headerLine;
        while (!(headerLine = bufferedReader.readLine()).equals("")) {
            if (isFirstLine) {
                String[] split = headerLine.split(" ");
                method = HttpMethod.parseOrNullValue(split[0]);
                queryString = split[1];
                isFirstLine = false;
            } else {
                Matcher matcher = pattern.matcher(headerLine);
                if (matcher.matches()) {
                    result.put(matcher.group(1), matcher.group(2));
                } else {
                    log.error(String.format("Can not parse header line: [ %s ]", headerLine));
                }
            }
        }
        return result;
    }

    private String readBody(BufferedReader bufferedReader, int length) throws IOException {
        char[] buffer = new char[length];
        int read = bufferedReader.read(buffer, 0, length);
        return new String(buffer, 0, read);
    }

    public int getContentLength() {
        if (headers.containsKey("Content-Length")) {
            return Integer.parseInt(headers.get("Content-Length"));
        }
        return 0;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public String getQueryString() {
        return queryString;
    }
}
