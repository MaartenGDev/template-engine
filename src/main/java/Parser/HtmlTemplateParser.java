package Parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.codemodel.internal.JArray;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlTemplateParser implements ITemplateParser {

    @Override
    public String parse(String template, JsonObject parameters) throws NoSuchFieldException {

        template = replaceForLoops(template, parameters);
        template = replacePlaceholders(template, getKeyValueFromJsonObject(parameters));

        return template;
    }

    private String replacePlaceholders(String template, Map<String, String> parameters) {
        Matcher m = Pattern.compile("(\\{\\{ *([a-zA-Z]+) *}})")
                .matcher(template);

        while (m.find()) {
            template = template.replace(m.group(1), parameters.get(getVariableName(m.group(2))));
        }

        return template;
    }

    private String replaceForLoops(String template, JsonObject parameters) throws NoSuchFieldException {
        Matcher m = Pattern.compile("(\\{% for (\\w+) %}(.*)\\{% endfor %})")
                .matcher(template);

        while (m.find()) {
            String matchedForLoop = m.group(1);
            String collectionName = m.group(2);
            String output = m.group(3).trim();

            JsonArray forItems = parameters.getAsJsonArray(collectionName);

            for (JsonElement currentElement : forItems) {
                JsonObject currentJsonObject = currentElement.getAsJsonObject();

                Map<String, String> forKeyValuePairs = new HashMap<>();

                for (String key : currentJsonObject.keySet()) {
                    String value = currentJsonObject.get(key).getAsString();

                    forKeyValuePairs.put(key, value);
                }

                template = template.replace(matchedForLoop, replacePlaceholders(output, forKeyValuePairs));
            }

        }

        return template;
    }

    private Map<String, String> getKeyValueFromJsonObject(JsonObject jsonObject) {
        Map<String, String> keyValuePairs = new HashMap<>();

        for (String key : jsonObject.keySet()) {
            if (!jsonObject.get(key).isJsonArray()) {
                keyValuePairs.put(key, jsonObject.get(key).getAsString());
            }
        }

        return keyValuePairs;
    }

    private String getVariableName(String placeholder) {
        return placeholder.replace("{", "").replace("}", "");
    }
}
