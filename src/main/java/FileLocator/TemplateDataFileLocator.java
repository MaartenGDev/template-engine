package FileLocator;

import java.io.IOException;

public class TemplateDataFileLocator extends FileLocator {

    public TemplateDataFileLocator(String basePath) {
        super(basePath);
    }

    @Override
    public String get(String name) throws IOException {
        return get("templateData", name);
    }
}
