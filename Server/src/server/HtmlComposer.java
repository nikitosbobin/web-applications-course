package server;

import core.ILog;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HtmlComposer implements IHtmlComposer {
    private String templateName;
    private ILog log;
    private Configuration configuration;

    public HtmlComposer(String frontPath, String templateName, ILog log) throws IOException {
        this.templateName = templateName;
        this.log = log;
        File front = new File(String.format("%s\\templates", frontPath));
        if (!front.exists()) {
            throw new FileNotFoundException(String.format(
                    "Templates folder does not exist: %s",
                    front.getAbsolutePath()));
        }
        configuration = new Configuration();
        //configuration.setDefaultEncoding("ascii");
        configuration.setDirectoryForTemplateLoading(front);
    }

    @Override
    public byte[] getHtmlBytes(TemplateModel model) {
        Template template;
        try {
            template = configuration.getTemplate(templateName);
        } catch (IOException e) {
            log.error(String.format("Composing html ends with error: %s", e.getMessage()));
            return new byte[0];
        }
        Map<String, TemplateModel> data = new HashMap<>();
        data.put("model", model);
        ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(memoryStream);
        try {
            template.process(data, streamWriter);
            //template.process(data, new OutputStreamWriter(System.out));
            streamWriter.flush();
        } catch (TemplateException | IOException e) {
            log.error(String.format("Composing html ends with error: %s", e.getMessage()));
            return new byte[0];
        }
        return memoryStream.toByteArray();
    }
}
