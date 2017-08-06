package Parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;
import java.util.Map;

public class JsonResourceFileParser implements IResourceFileParser {
    @Override
    public Map<String, Object> parse(String content) {
        JsonParser parser = new JsonParser();

        Map<String, Object> values = new HashMap<>();

        JsonObject jsonObject = parser.parse(content).getAsJsonObject();

        for (String objectKey : jsonObject.keySet()) {
            JsonElement value = jsonObject.get(objectKey);

            values.put(objectKey, getPrimitiveAsObject(value));
        }

        return values;
    }

    private Object getPrimitiveAsObject(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive valueType = element.getAsJsonPrimitive();

            if (valueType.isBoolean()) {
                return element.getAsBoolean();
            } else if (valueType.isString()) {
                return element.getAsString();
            } else if (valueType.isNumber()) {
                return element.getAsNumber();
            }
        } else if (element.isJsonArray()) {
            return element.getAsJsonArray();
        }

        return element.getAsJsonObject();
    }
}
