package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SuppressWarnings("ConstantConditions")
public class ResourceUtils {

    public static String loadStringResource(String fileName) throws IOException, ClassNotFoundException, NullPointerException {
        ClassLoader classLoader = Class.forName(ResourceUtils.class.getName()).getClassLoader();

        File file = new File(classLoader.getResource(fileName).getFile());

        return new String(Files.readAllBytes(file.toPath()));
    }
}
