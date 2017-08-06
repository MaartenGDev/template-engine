package Parser;

import com.google.gson.JsonObject;

import java.util.Map;

public interface IResourceFileParser {
    Map<String, Object> parse(String content);
}
