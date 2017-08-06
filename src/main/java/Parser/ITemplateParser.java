package Parser;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

public interface ITemplateParser {
    String parse(String template, Map<String, Object> parameters) throws NoSuchFieldException, IOException;
}
