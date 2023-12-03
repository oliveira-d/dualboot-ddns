import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private Properties properties;

    public ConfigReader() {
        properties = new Properties();
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void readConfig(String filePath) {
        try (FileInputStream input = new FileInputStream(filePath)) {
            System.out.println("Opening configuration file...");
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
