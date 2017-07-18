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
        template = replaceIfStatements(template, parameters);
        template = replacePlaceholders(template, getKeyValueFromJsonObject(parameters));

        return template;
    }

    private String replacePlaceholders(String template, Map<String, String> parameters) {
        Matcher m = Pattern.compile("(\\{\\{ *([a-zA-Z.]+) *}})")
                .matcher(template);

        while (m.find()) {
            String placeholder = m.group(1);
            String variableKey = m.group(2);

            template = template.replace(placeholder, parameters.get(variableKey));
        }

        return template;
    }

    private String replaceForLoops(String template, JsonObject parameters) throws NoSuchFieldException {
        Matcher m = Pattern.compile("(\\{% for (\\w+) in (\\w+) %}(.*)\\{% endfor %})")
                .matcher(template);

        while (m.find()) {
            String matchedForLoop = m.group(1);
            String fieldName = m.group(2);
            String collectionName = m.group(3);
            String output = m.group(4).trim();

            StringBuilder forItemBuilder = new StringBuilder();

            JsonArray forItems = parameters.getAsJsonArray(collectionName);

            for (JsonElement currentElement : forItems) {
                JsonObject currentJsonObject = currentElement.getAsJsonObject();

                Map<String, String> forKeyValuePairs = new HashMap<>();

                for (String key : currentJsonObject.keySet()) {
                    String value = currentJsonObject.get(key).getAsString();

                    forKeyValuePairs.put(fieldName + "." + key, value);
                }

                forItemBuilder.append(replacePlaceholders(output, forKeyValuePairs));
            }

            template = template.replace(matchedForLoop, forItemBuilder);
        }

        return template;
    }

    private String replaceIfStatements(String template, JsonObject parameters) {
        Matcher m = Pattern.compile("(\\{% if (?:not )?(?:\\w+) %}.*\\{% endif %})")
                .matcher(template);


        while (m.find()) {
            String ifStatement = m.group();

            Matcher ifGroups = Pattern.compile("(\\{% ((?:else)?(?:if)?) (not )?(\\w+) %})([^{%]+)")
                    .matcher(template);

            boolean hasFoundTruthyIfStatement = false;

            while (ifGroups.find()) {
                String ifType = ifGroups.group(2);
                boolean isNegatedIf = ifGroups.group(3) != null;
                String output = ifGroups.group(5);

                boolean ifRequirementIsTruthy = parameters.get(ifGroups.group(4)).getAsBoolean();

                boolean evaluatesToTrue = ifType.equals("else") || isNegatedIf != ifRequirementIsTruthy;

                if (evaluatesToTrue) {
                    hasFoundTruthyIfStatement = true;
                    template = template.replace(ifStatement, replacePlaceholders(output, getKeyValueFromJsonObject(parameters)));

                    break;
                }
            }

            if (!hasFoundTruthyIfStatement) {
                template = template.replace(ifStatement, "");
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
}
