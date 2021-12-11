package project.lab6.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {

    public static String CONFIG_LOCATION = Config.class.getClassLoader()
            .getResource("project/lab6/config.properties").getPath().replace("%20", " ");

    public static Properties getProperties() {
        Properties properties = new Properties();
        try {
            try (FileReader reader = new FileReader(CONFIG_LOCATION)) {
                properties.load(reader);
            }
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config properties");
        }
    }
}
