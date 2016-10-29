package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.ILog;
import core.Individual_3;
import core.Result;
import models.*;
import org.hibernate.criterion.Restrictions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class HttpServerHandler implements IHttpServerHandler {
    private Path rootFilesDirectory;
    private ILog log;
    private IHtmlComposer htmlComposer;

    public HttpServerHandler(Path rootFilesDirectory, IHtmlComposer htmlComposer, ILog log) {
        this.rootFilesDirectory = rootFilesDirectory;
        this.log = log;
        this.htmlComposer = htmlComposer;
    }

    @Override
    public Response handle(Request request) {
        HttpMethod httpMethod = request.getMethod();
        if (httpMethod == null){
            return Response.empty();
        }
        switch (httpMethod) {
            case GET:
                return handleGet(request);
            case POST:
                return handlePost(request);
            default: throw new RuntimeException("Not supported http method: " + httpMethod
                    + " in HttpServerHandler");
        }
    }

    private Response handlePost(Request request) {
        String queryString = request.getQueryString();
        if (queryString.length() > 0) {
            Result individual = new Individual_3(request.getBody());
            try {
                String result = individual.getResult();
                return new Response(result.getBytes("utf-8"), MimeType.html);
            } catch (UnsupportedEncodingException e) {
                log.error(String.format("Handling POST request ends with error: %s", e.getMessage()));
                return Response.empty();
            }
        }
        return Response.empty();
    }

    private Response handleGet(Request request) {
        String queryString = request.getQueryString();
        if (queryString.equals("/")) {
            queryString = "/index.html";
        }
        if (queryString.contains("?")) {
            queryString = queryString.substring(0, queryString.indexOf('?'));
        }
        Path possibleFile = Paths.get(rootFilesDirectory.toString(), queryString);
        if (possibleFile.toFile().exists()){
            try {
                String fileType = getFileType(possibleFile);
                byte[] body;
                if (fileType.equals("html")) {
                    body = generateHtml(possibleFile);
                }else {
                    body = Files.readAllBytes(possibleFile);
                }
                return new Response(body, MimeType.valueOf(fileType));
            } catch (IOException | IllegalArgumentException e) {
                log.error(String.format("Handling GET request ends with error: %s", e.getMessage()));
                return Response.empty();
            }
        }
        try {
            return performDbOperation(queryString);
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            log.error(String.format("Handling GET request ends with error: %s", e.getMessage()));
            return Response.empty();
        }
    }

    private Response performDbOperation(String query) throws JsonProcessingException, UnsupportedEncodingException {
        String[] segments = query.split("/");
        if (!segments[1].equals("db")) {
            return Response.empty();
        }
        switch (segments[2]) {
            case "get-details":
                return getDetails();
            case "get-detail":
                return getDetail(Integer.parseInt(segments[3]));
            case "delete-detail":
                return deleteDetail(Integer.parseInt(segments[3]));
            default: return Response.empty();
        }
    }

    private Response deleteDetail(int id) throws UnsupportedEncodingException {
        boolean b = DbContext.deleteEntity(Detail.class, id);
        return new Response((b + "").getBytes("UTF-8"), MimeType.html);
    }

    private Response getDetail(int id) throws JsonProcessingException, UnsupportedEncodingException {
        Detail detail = DbContext.getEntityById(Detail.class, id);
        List<DesignersGroup> designersGroups = DbContext
                .getEntities(DesignersGroup.class, Restrictions.eq("id", detail.getDesigners_groups_id()));
        List<Warehouse> warehouses = DbContext.getEntities(Warehouse.class, Restrictions.eq("id", detail.getWarehouses_id()));
        List<Factory> factories = DbContext.getEntities(Factory.class, Restrictions.eq("id", detail.getFactories_id()));
        String warehouse = String.format("%s %s", warehouses.get(0).getCity(), warehouses.get(0).getAdress());
        String factory = String.format("%s %s", factories.get(0).getCity(), factories.get(0).getAdress());
        DetailSpecialty detailSpecialty = new DetailSpecialty(detail, warehouse, factory, designersGroups.get(0).getDesigners());
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(detailSpecialty);
        return new Response(s.getBytes("UTF-8"), MimeType.json);
    }

    private Response getDetails() throws JsonProcessingException, UnsupportedEncodingException {
        List<Detail> allDetails = DbContext.getEntities(Detail.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(allDetails);
        return new Response(s.getBytes("UTF-8"), MimeType.json);
    }

    private byte[] generateHtml(Path file) throws IOException {
        TemplateModel model = new TemplateModel();
        String fileName = getFileName(file);
        String title = "";
        String indexActiveClass = "";
        String individualActiveClass = "";
        String tablesActiveClass = "";
        switch (fileName) {
            case "index":
                title = "About";
                indexActiveClass = "active";
                break;
            case "tables":
                title = "Tables browser";
                tablesActiveClass = "active";
                break;
            case "individual":
                title = "Individual task";
                individualActiveClass = "active";
                break;
        }
        model.setTitle(title);
        model.setContent(toOneString(Files.readAllLines(file, Charset.forName("UTF-8"))));
        model.setIndexActiveClass(indexActiveClass);
        model.setTablesActiveClass(tablesActiveClass);
        model.setIndividualActiveClass(individualActiveClass);
        return htmlComposer.getHtmlBytes(model);
    }

    private String toOneString(List<String> lines) {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line);
            builder.append("\r\n");
        }
        String result = builder.toString();
        return result;
    }

    private String toRedLine(String line) {
        if (line.length() == 0) {
            return "";
        }
        if (line.length() == 1) {
            return line.toUpperCase();
        }
        return String.format("%s%s", (line.charAt(0) + "").toUpperCase(), line.substring(1));
    }

    private String getFileName(Path path) {
        String fileName = path.getFileName().toString();
        int pointPosition = fileName.lastIndexOf('.');
        return fileName.substring(0, pointPosition);
    }

    private String getFileType(Path path) {
        String fileName = path.getFileName().toString();
        int pointPosition = fileName.lastIndexOf('.');
        return fileName.substring(pointPosition + 1);
    }
}
