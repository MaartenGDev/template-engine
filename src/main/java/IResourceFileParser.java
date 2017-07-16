import java.nio.file.Path;
import java.util.Map;

public interface IResourceFileParser {
    Map<String, String> parse(String content);
}
