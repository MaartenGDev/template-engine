import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jdk.nashorn.internal.parser.JSONParser;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JsonResourceFileParser implements IResourceFileParser {
    @Override
    public Map<String, String> parse(String content) {

        Gson gson = new Gson();

        Type collectionType = new TypeToken<HashMap<String, String>>(){}.getType();

        return gson.fromJson(content, collectionType);
    }
}
