package Parser;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            return convertJsonArrayToList(element.getAsJsonArray());
        }

        return convertJsonObjectToMap(element.getAsJsonObject());
    }

    private List<Object> convertJsonArrayToList(JsonArray jsonArray){
        List<Object> list = new ArrayList<>();

        for(JsonElement element: jsonArray){
            list.add(convertJsonObjectToMap(element.getAsJsonObject()));
        }

        return list;
    }

    private Map<String, Object> convertJsonObjectToMap(JsonObject jsonObject){
        Map<String, Object> map = new HashMap<>();

        for(String key : jsonObject.keySet()){
            map.put(key, getPrimitiveAsObject(jsonObject.get(key)));
        }

        return map;
    }
}
