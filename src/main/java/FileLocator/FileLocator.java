package FileLocator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class FileLocator implements IFileLocator {
    private String basePath;

    public FileLocator(String basePath){
        this.basePath = basePath;
    }

    public String get(String... arguments) throws IOException {
        return getFileContent(Paths.get(this.basePath, arguments));
    }

    private String getFileContent(Path path) throws IOException {
        return String.join("", Files.readAllLines(path));
    }
}
