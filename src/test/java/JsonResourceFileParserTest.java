import FileLocator.FileLocator;
import Parser.HtmlTemplateParser;
import Parser.JsonResourceFileParser;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class JsonResourceFileParserTest {
    private JsonResourceFileParser resourceFileParser;

    public JsonResourceFileParserTest() {
        resourceFileParser = new JsonResourceFileParser();
    }

    @Test
    public void keyValuePairsResultInAHashMap(){
        Map<String, Object> actual = resourceFileParser.parse("{\"name\": \"helloWorld\"}");

        Map<String, Object> expected = new HashMap<>();
        expected.put("name", "helloWorld");

        assertEquals(expected, actual);
    }

    @Test
    public void jsonListGetsConvertedToArrayList(){
        Map<String, Object> actual = resourceFileParser.parse("{\"users\": [{\"name\": \"first\"},{\"name\": \"second\"}]}");

        Map<String, Object> expected = new HashMap<>();
        List<Map<String, Object>> users = new ArrayList<>();

        HashMap<String, Object> firstUser = new HashMap<>();
        firstUser.put("name", "first");

        HashMap<String, Object> secondUser = new HashMap<>();
        secondUser.put("name", "second");

        users.add(firstUser);
        users.add(secondUser);

        expected.put("users", users);

        assertEquals(expected, actual);
    }

    @Test
    public void jsonObjectGetsConvertedToHashMap(){
        Map<String, Object> actual = resourceFileParser.parse("{\"user\": {\"name\": \"first\", \"level\": 6}}");

        Map<String, Object> expected = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        user.put("name", "first");
        user.put("level", 6);

        expected.put("user", user);

        assertEquals(expected, actual);
    }
}
