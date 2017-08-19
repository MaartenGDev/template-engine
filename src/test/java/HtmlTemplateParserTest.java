import Parser.HtmlTemplateParser;
import Parser.JsonResourceFileParser;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import FileLocator.FileLocator;

import static junit.framework.TestCase.assertEquals;

public class HtmlTemplateParserTest {

    private HtmlTemplateParser templateParser;

    public HtmlTemplateParserTest() {
        String basePath = getProjectRoot();

        FileLocator templateLocator = new FileLocator(basePath);

        templateParser = new HtmlTemplateParser(templateLocator);
    }
    private static String getProjectRoot() {
        return Paths.get(".", "src/main/resources").toString();
    }

    @Test
    public void templateParserFillsPlaceHolderInH1Element() throws NoSuchFieldException, IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "helloWorld");

        String output = templateParser.parse("<h1>{{name}}</h1>", parameters);

        assertEquals("<h1>helloWorld</h1>", output);
    }

    @Test
    public void ifBlockContentIsOnlyShownWhenTruthy() throws NoSuchFieldException, IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("isLoggedIn", true);

        String output = templateParser.parse("<section>{% if isLoggedIn %}<p>Welcome</p>{% endif %}</section>", parameters);

        assertEquals("<section><p>Welcome</p></section>", output);
    }

    @Test
    public void ifBlockContentIsOnlyShownWhenTruthyWithNegatedIf() throws NoSuchFieldException, IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("isLoggedIn", false);

        String output = templateParser.parse("<section>{% if not isLoggedIn %}<p>Not logged in</p>{% endif %}</section>", parameters);

        assertEquals("<section><p>Not logged in</p></section>", output);
    }

    @Test
    public void onlyContentOfFirstTruthyIfIsShown() throws NoSuchFieldException, IOException {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("isLoggedIn", false);
        parameters.put("isAdmin", true);

        String output = templateParser.parse("<section>{% if isLoggedIn %}<p>Not logged in</p>{% elseif isAdmin %}<p>Hello non authenticated admin</p>{% endif %}</section>", parameters);

        assertEquals("<section><p>Hello non authenticated admin</p></section>", output);
    }

    @Test
    public void contentOfElseIsShownWhenThereAreNoTruthyIfStatements() throws NoSuchFieldException, IOException {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("isLoggedIn", false);
        parameters.put("isAdmin", false);

        String output = templateParser.parse("<section>{% if isLoggedIn %}<p>Not logged in</p>{% elseif isAdmin %}<p>Hello non authenticated admin</p>{% else %}<p>Not logged in and no admin</p>{% endif %}</section>", parameters);

        assertEquals("<section><p>Not logged in and no admin</p></section>", output);
    }
    @Test
    public void propertiesOfObjectInListGetUsedForPlaceholdersInForBlock() throws NoSuchFieldException, IOException {
        Map<String, Object> parameters = new HashMap<>();
        List<Map<String, Object>> users = new ArrayList<>();

        HashMap<String, Object> firstUser = new HashMap<>();
        firstUser.put("name", "First User");

        HashMap<String, Object> secondUser = new HashMap<>();
        secondUser.put("name", "Second User");

        users.add(firstUser);
        users.add(secondUser);

        parameters.put("users", users);

        String output = templateParser.parse("<ul>{% for user in users %}<li>{{user.name}}</li>{% endfor %}</ul>", parameters);

        assertEquals("<ul><li>First User</li><li>Second User</li></ul>", output);
    }
}