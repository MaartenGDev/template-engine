package Parser;

import com.google.gson.JsonObject;

public interface IResourceFileParser {
    JsonObject parse(String content);
}
