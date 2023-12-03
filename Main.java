//user for checking wether configuration files and paths exist
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//used by NoIP updater
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

// getting input to write configuration file
import java.util.Scanner;

// used to check write privileges
import java.nio.file.FileSystems;
//import java.nio.file.Files;
import java.nio.file.LinkOption;
//import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        String configFolderName0 = "/usr/local/etc";
        String configFolderName1 = "/etc";
        String configFileName = "dydns.conf";
        String configFolder = "";
        String configFilePath = "";
        String[] configurationParameters = {"username","password","hostname","domain"};

        Path configPath0 = Paths.get(configFolderName0);
        Path configPath1 = Paths.get(configFolderName1);
         

        if (Files.exists(configPath0) && Files.isDirectory(configPath0)){
            configFolder = configFolderName0;
        } else if (Files.exists(configPath1) && Files.isDirectory(configPath1)) {
            configFolder = configFolderName1;
        } else {
            System.out.println("No configuration folder found: could not locate "+configFolderName0+" or "+configFolderName1);
            System.out.println("Configuration file "+configFileName+" will be written in the same folder as the executable.");
            configFolder = ".";
        }
        configFilePath = configFolder + "/" + configFileName;

        Path configFile = Paths.get(configFilePath);
        
        if (!Files.exists(configFile)) {
            System.err.println("Configuration file not found in "+configFilePath);
            if (!hasWritePrivileges(configFolder)){
                System.err.println("No write privileges to "+configFolder);
                System.err.println("Try running the program as root to write the configuration file.");
                System.exit(0);
            }
            System.out.println("Writing config...");

            ConfigWriter configWriter = new ConfigWriter();

            // Set properties
            Scanner scanner = new Scanner(System.in);
            String value;
            for (int i=0; i<configurationParameters.length; i++){
                System.out.println("Enter "+configurationParameters[i]+":");
                value = scanner.nextLine();
                configWriter.setProperty(configurationParameters[i],value);
            }

            // Write to config file
            configWriter.writeConfig(configFilePath);
            
            System.out.println("Configuration file created at "+configFilePath);
            System.out.println("Run the program again to update you DDNS records.");
        } else {
            ConfigReader configReader = new ConfigReader();
            configReader.readConfig(configFilePath);

            String username = configReader.getProperty("username");
            String password = configReader.getProperty("password");
            String hostname = configReader.getProperty("hostname");
            String domain = configReader.getProperty("domain");
            //procedes to update DDNS
            //System.out.println(username+" "+password+" "+hostname+" "+domain);
            try {
                String currentIP = getCurrentIP();
                NoIp.update(username, password, hostname, domain, currentIP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getCurrentIP() throws IOException {
        URL url = new URL("https://ifconfig.me/");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
            return reader.readLine().trim();
        }
    }

    public static boolean hasWritePrivileges(String configFolder) {
    
        Path path = FileSystems.getDefault().getPath(configFolder);

        if (Files.isWritable(path)) {
            return true;
        } else {
            return false;
        }
    }

}
