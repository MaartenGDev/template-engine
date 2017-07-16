import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public interface ITemplateParser {
    String parse(String template, Map<String, String> parameters);
}
