import FileLocator.TemplateFileLocator;
import Parser.HtmlTemplateParser;
import Database.Database;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException, NoSuchFieldException, SQLException {
        String basePath = getProjectRoot();

        TemplateFileLocator templateLocator = new TemplateFileLocator(basePath);


        Database database = new Database("jdbc:mysql://localhost:3306/templates", "root", "");


        Map<String, Object> parameters = new HashMap<>();

        try(Connection connection = database.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM threads")){
                ResultSet resultSet = statement.executeQuery();
                List<Map<String, Object>> list = new ArrayList<>();

                while(resultSet.next()) {
                    String name = resultSet.getString("name");
                    String body = resultSet.getString("body");


                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("body", body);

                    list.add(map);

                }
                parameters.put("threads", list);
            }
        }

        HtmlTemplateParser parser = new HtmlTemplateParser(templateLocator);

        System.out.println(parser.parse(templateLocator.get("index.mct"), parameters));
    }


    private static String getProjectRoot() {
        return Paths.get(".", "src/main/resources").toString();
    }
}
