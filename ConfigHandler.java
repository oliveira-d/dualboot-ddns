import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

// getting input to write configuration file
import java.util.Scanner;

public class ConfigHandler {

    private Properties properties;
    private String filePath; 

    public ConfigHandler(String filePath) {
        properties = new Properties();
        this.filePath = filePath;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public void readConfig() {
        try (FileInputStream input = new FileInputStream(this.filePath)) {
            System.out.println("Opening configuration file...");
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeConfig() {
        try (FileOutputStream output = new FileOutputStream(this.filePath)) {
            properties.store(output, "Configuration Properties");
            System.out.println("Config file written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createConfig(String[] configurationParameters) {
        
        Scanner scanner = new Scanner(System.in);
        String value;
        for (int i=0; i<configurationParameters.length; i++){
            System.out.println("Enter "+configurationParameters[i]+":");
            value = scanner.nextLine();
            this.setProperty(configurationParameters[i],value);
        }

        // Write to config file
        this.writeConfig();
    }
}
