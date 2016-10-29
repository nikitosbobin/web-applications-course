package server;

import core.ILog;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class Server {
    private ServerSocket serverSocket;
    private ILog log;
    private Thread worker;
    private boolean started;
    private IHttpServerHandler handler;

    public Server(int port, IHttpServerHandler handler, ILog log) throws IOException {
        this(port, 1, handler, log);
    }

    public Server(int port, int backLog, IHttpServerHandler handler, ILog log) throws IOException {
        serverSocket = new ServerSocket(port, backLog);
        this.log = log;
        this.handler = handler;
        worker = new Thread(() -> {
            try {
                internalStart();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void start() {
        if (!started) {
            started = true;
            worker.start();
            log.info("started");
        }
    }

    public void stop() {
        if (started && worker.isAlive()) {
            started = false;
            worker.interrupt();
        }
    }

    private byte[] getHeadersBlock(int contentLength, MimeType mimeType) {
        String result = "HTTP/1.1 200 OK\r\n" +
                "Server: NikitServer\r\n" +
                "Content-Type: " + mimeType.getValue() + "\r\n" +
                "Content-Length: " + contentLength + "\r\n" +
                "Connection: close\r\n\r\n";
        return result.getBytes(Charset.forName("UTF-8"));
    }

    private void internalStart() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            Thread clientWorker = new Thread(() -> {
                try {
                    handleClient(socket);
                } catch (IOException | InterruptedException e) {
                    log.error(e.getMessage());
                }
            });
            clientWorker.start();
        }
    }

    private void handleClient(Socket client) throws IOException, InterruptedException {
        log.info("request accepted at thread: " + Thread.currentThread().getId());
        Request request = new Request(client.getInputStream(), log);
        log.info("method: " + request.getMethod());
        log.info("query string: " + request.getQueryString());
        Response handleResult = handler.handle(request);

        byte[] headers = getHeadersBlock(handleResult.getResultLength(), handleResult.getMimeType());
        byte[] body = handleResult.getResponseBody();

        OutputStream outputStream = client.getOutputStream();

        outputStream.write(headers);
        outputStream.write(body);

        outputStream.flush();

        log.info("request accepted");
        client.close();
    }
}
