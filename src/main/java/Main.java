import FileLocator.TemplateDataFileLocator;
import FileLocator.TemplateFileLocator;
import Parser.HtmlTemplateParser;
import Parser.JsonResourceFileParser;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException, NoSuchFieldException {
        String basePath = Paths.get(getProjectRoot()).toString();

        TemplateFileLocator templateLocator = new TemplateFileLocator(basePath);

        TemplateDataFileLocator templateDataFileLocator = new TemplateDataFileLocator(basePath);

        JsonResourceFileParser resourceFileParser = new JsonResourceFileParser();

        JsonObject parameters = resourceFileParser.parse(templateDataFileLocator.get("index.json"));

        HtmlTemplateParser parser = new HtmlTemplateParser();

        System.out.println(parser.parse(templateLocator.get("index.mct"), parameters));
    }


    private static String getProjectRoot() {
        return Paths.get(".", "src/main/resources").toString();
    }
}
