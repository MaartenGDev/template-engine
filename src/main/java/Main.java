import Parser.HtmlTemplateParser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import FileLocator.FileLocator;

public class Main {

    public static void main(String[] args) throws IOException, NoSuchFieldException, SQLException {
        String basePath = "/Users/maarten/Documents/prive/Projects/forum-charterstone/source/src/pages";

        FileLocator templateLocator = new FileLocator(basePath);

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("isLoggedIn", true);
        List<Map<String, Object>> list = new ArrayList<>();

        HashMap<String, Object> firstPost = new HashMap<>();
        firstPost.put("title", "Hello world");
        firstPost.put("user", "MaartenGDev");
        firstPost.put("date", "2017-12-13");
        firstPost.put("body", "my post");

        HashMap<String, Object> secondPost = new HashMap<>();
        secondPost.put("title", "Second First");
        secondPost.put("user", "Admin");
        secondPost.put("date", "2017-11-13");
        secondPost.put("body", "second post");

        list.add(firstPost);
        list.add(secondPost);

        parameters.put("posts", list);

        HtmlTemplateParser parser = new HtmlTemplateParser(templateLocator);

        System.out.println(parser.parse(templateLocator.get("index.mct"), parameters));
    }
}
