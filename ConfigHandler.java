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
            properties.load(input);
        } catch (IOException e) {
            //e.printStackTrace();
            Main.logger.warning("Failed to open configuration file for reading!");
        }
    }

    public void writeConfig() {
        try (FileOutputStream output = new FileOutputStream(this.filePath)) {
            properties.store(output, "Configuration Properties");
            Main.logger.info("Configuration file written successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            Main.logger.warning("Failed to write configuration file!");
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
