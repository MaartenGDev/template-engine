package Parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonResourceFileParser implements IResourceFileParser {
    @Override
    public JsonObject parse(String content) {
        JsonParser parser = new JsonParser();

        return parser.parse(content).getAsJsonObject();
    }
}
