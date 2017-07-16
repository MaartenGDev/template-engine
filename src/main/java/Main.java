import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        JsonResourceFileParser resourceFileParser = new JsonResourceFileParser();

        Map<String, String> parameters  = resourceFileParser.parse(getResourceFileContent("index.json"));

        HtmlTemplateParser parser = new HtmlTemplateParser();

        System.out.println(parser.parse(Main.getTemplateContent("index.mct"),parameters));
    }

    private static String getTemplateContent(String name) throws IOException {
        Path path = Paths.get(getProjectRoot(), "templates",name).toAbsolutePath().normalize();

        return String.join("", Files.readAllLines(path));
    }

    private static String getResourceFileContent(String name) throws IOException {
        Path path = Paths.get(getProjectRoot(), "templateData",name).toAbsolutePath().normalize();

        return String.join("", Files.readAllLines(path));
    }

    private static String getProjectRoot(){
        return Paths.get(".", "src/main/resources").toString();
    }
}
