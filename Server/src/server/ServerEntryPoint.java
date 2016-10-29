package server;

import core.ConsoleLog;
import core.ILog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerEntryPoint {
    public static void main(String[] args) throws IOException {
        String frontPath = new File("front").getAbsolutePath();
        ConsoleLog log = new ConsoleLog();
        HtmlComposer composer = new HtmlComposer(frontPath, "layout.html", log.withPrefix("Composer"));
        IHttpServerHandler handler = new HttpServerHandler(Paths.get(frontPath), composer, log.withPrefix("Handler"));
        Server server = new Server(8080, handler, log.withPrefix("Server"));
        server.start();
    }
}
