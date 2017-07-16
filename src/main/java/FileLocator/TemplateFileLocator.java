package FileLocator;

import java.io.IOException;

public class TemplateFileLocator extends FileLocator {

    public TemplateFileLocator(String basePath) {
        super(basePath);
    }

    @Override
    public String get(String name) throws IOException {
        return get("templates", name);
    }
}
