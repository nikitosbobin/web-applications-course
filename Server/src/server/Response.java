package server;

public class Response {
    private byte[] result;
    private MimeType mimeType;

    public Response(byte[] result, MimeType mimeType) {
        this.result = result;
        this.mimeType = mimeType;
    }

    public byte[] getResponseBody() {
        return result;
    }

    public int getResultLength() {
        return result.length;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    public static Response empty() {
        return new Response(new byte[0], MimeType.html);
    }
}
