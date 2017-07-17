package Parser;

import com.google.gson.JsonObject;

import java.util.Map;

public interface ITemplateParser {
    String parse(String template, JsonObject parameters) throws NoSuchFieldException;
}
