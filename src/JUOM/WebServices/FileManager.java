package JUOM.WebServices;

import java.io.IOException;
import java.io.InputStream;

public class FileManager {

    public static byte[] readFile(String path, Class<?> clazz) throws IOException {
        try (InputStream e = clazz.getResourceAsStream(path)) {
            if (e == null) {
                return null;
            }
            return e.readAllBytes();
        }
    }

    public static String[] getAllFiles(String path, Class<?> clazz) throws IOException {
        if(clazz.getResource(path) == null) {
            throw new IOException("Resource not found: " + path);
        }
        java.nio.file.Path resourcePath = java.nio.file.Paths.get(clazz.getResource("/").getPath(), path);
        return java.nio.file.Files.walk(resourcePath)
                .filter(java.nio.file.Files::isRegularFile)
                .map(java.nio.file.Path::toString)
                .toArray(String[]::new);
    }

    public static void writeFile(String path, byte[] bytes, Class<?> clazz) throws IOException {
        if(clazz.getResource(path) == null) {
            throw new IOException("Resource not found: " + path);
        }
        java.nio.file.Path resourcePath = java.nio.file.Paths.get(clazz.getResource("/").getPath(), path);
        java.nio.file.Files.createDirectories(resourcePath.getParent());
        java.nio.file.Files.write(resourcePath, bytes);
    }

}
