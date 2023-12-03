import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigWriter {

    private Properties properties;

    public ConfigWriter() {
        properties = new Properties();
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public void writeConfig(String filePath) {
        try (FileOutputStream output = new FileOutputStream(filePath)) {
            properties.store(output, "Configuration Properties");
            System.out.println("Config file written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
