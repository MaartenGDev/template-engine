package FileLocator;

import java.io.IOException;

public interface IFileLocator {
    String get(String name) throws IOException;
}
