import Parser.HtmlTemplateParser;
import Parser.JsonResourceFileParser;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import FileLocator.FileLocator;

import static junit.framework.TestCase.assertEquals;

public class HtmlTemplateParserTest {

    private HtmlTemplateParser templateParser;
    private JsonResourceFileParser resourceFileParser;

    public HtmlTemplateParserTest() {
        String basePath = getProjectRoot();

        FileLocator templateLocator = new FileLocator(basePath);

        templateParser = new HtmlTemplateParser(templateLocator);
        resourceFileParser = new JsonResourceFileParser();
    }
    private static String getProjectRoot() {
        return Paths.get(".", "src/main/resources").toString();
    }

    @Test
    public void templateParserFillsPlaceHolderInH1Element() throws NoSuchFieldException, IOException {
        Map<String, Object> parameters = resourceFileParser.parse("{\"name\": \"helloWorld\"}");

        String output = templateParser.parse("<h1>{{name}}</h1>", parameters);

        assertEquals("<h1>helloWorld</h1>", output);
    }

    @Test
    public void ifBlockContentIsOnlyShownWhenTruthy() throws NoSuchFieldException, IOException {
        Map<String, Object> parameters = resourceFileParser.parse("{\"isLoggedIn\": true}");

        String output = templateParser.parse("<section>{% if isLoggedIn %}<p>Welcome</p>{% endif %}</section>", parameters);

        assertEquals("<section><p>Welcome</p></section>", output);
    }

    @Test
    public void ifBlockContentIsOnlyShownWhenTruthyWithNegatedIf() throws NoSuchFieldException, IOException {
        Map<String, Object> parameters = resourceFileParser.parse("{\"isLoggedIn\": false}");

        String output = templateParser.parse("<section>{% if not isLoggedIn %}<p>Not logged in</p>{% endif %}</section>", parameters);

        assertEquals("<section><p>Not logged in</p></section>", output);
    }

    @Test
    public void onlyContentOfFirstTruthyIfIsShown() throws NoSuchFieldException, IOException {
        Map<String, Object> parameters = resourceFileParser.parse("{\"isLoggedIn\": false, \"isAdmin\": true}");

        String output = templateParser.parse("<section>{% if isLoggedIn %}<p>Not logged in</p>{% elseif isAdmin %}<p>Hello non authenticated admin</p>{% endif %}</section>", parameters);

        assertEquals("<section><p>Hello non authenticated admin</p></section>", output);
    }

    @Test
    public void contentOfElseIsShownWhenThereAreNoTruthyIfStatements() throws NoSuchFieldException, IOException {
        Map<String, Object> parameters = resourceFileParser.parse("{\"isLoggedIn\": false, \"isAdmin\": false}");

        String output = templateParser.parse("<section>{% if isLoggedIn %}<p>Not logged in</p>{% elseif isAdmin %}<p>Hello non authenticated admin</p>{% else %}<p>Not logged in and no admin</p>{% endif %}</section>", parameters);

        assertEquals("<section><p>Not logged in and no admin</p></section>", output);
    }
    @Test
    public void propertiesOfObjectInListGetUsedForPlaceholdersInForBlock() throws NoSuchFieldException, IOException {
        Map<String, Object> parameters = resourceFileParser.parse("{\"users\": [{\"name\": \"First User\"}, {\"name\": \"Second User\"}]}");

        String output = templateParser.parse("<ul>{% for user in users %}<li>{{user.name}}</li>{% endfor %}</ul>", parameters);

        assertEquals("<ul><li>First User</li><li>Second User</li></ul>", output);
    }
}