import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

// getting input to write configuration file
import java.util.Scanner;

public class ConfigHandler {

    private Properties properties;
    private String filePath;
    public static String[] DDNSProviders = {"No-IP","DynDNS","DuckDNS"};
    private String[] NoIPConfigurationParameters = {"username","password","hostname","domain"};
    private String[] DynDNSConfigurationParameters = {"username","updater client key","hostname","domain"};
    private String[] DuckDNSConfigurationParameters = {"hostname","token"};

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

    public void createConfig() {
        
        Scanner scanner = new Scanner(System.in);
        String value;
        System.out.printf("Enter your dynamic DNS service provider: %n1) %s%n2) %s%n3) %s%n",DDNSProviders[0],DDNSProviders[1],DDNSProviders[2]);
        int providerOption = scanner.nextInt();
        scanner.nextLine(); // needed to avoid newLine char to get read int nextLine() below
        String[] configurationParameters = {};
        switch (providerOption) {
            case 1:
                this.setProperty("provider",DDNSProviders[0]);
                configurationParameters = NoIPConfigurationParameters;
                break;
            case 2:
                this.setProperty("provider",DDNSProviders[1]);
                configurationParameters = DynDNSConfigurationParameters;
                break;
            case 3:
                this.setProperty("provider",DDNSProviders[2]);
                configurationParameters = DuckDNSConfigurationParameters;
                break;
            default:
                this.setProperty("provider",DDNSProviders[0]);
                configurationParameters = NoIPConfigurationParameters;
                Main.logger.info("Default provider set to "+DDNSProviders[0]);
        }
        for (int i=0; i<configurationParameters.length; i++){
            System.out.println("Enter "+configurationParameters[i]+":");
            value = scanner.nextLine();
            this.setProperty(configurationParameters[i],value);
        }

        // Write to config file
        this.writeConfig();
    }
}
