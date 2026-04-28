package configuration;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {

    Properties prop;

    public ConfigReader() {
        try {
            FileInputStream fis = new FileInputStream("src/test/java/configuration/config.properties");
            prop = new Properties();
            prop.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return prop.getProperty(key);
    }
}