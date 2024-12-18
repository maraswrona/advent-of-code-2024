package net.woroniecki;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    public static String readFileToString(String filePath) {
        URL resource = FileUtil.class.getResource(filePath);
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found: " + filePath);
        }

        try (InputStream is = resource.openStream()) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource: " + filePath, e);
        }
    }

}
