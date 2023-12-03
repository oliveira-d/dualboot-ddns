import java.io.IOException;

//user for checking wether configuration files and paths exist
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// used to check write privileges
import java.nio.file.FileSystems;
//import java.nio.file.Files;
import java.nio.file.LinkOption;
//import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        String[] configFolderNames = {"/usr/local/etc","/etc","."};
        String configFileName = "dydns.conf";
        String[] configurationParameters = {"username","password","hostname","domain"};

        Path[] configFolderPaths = new Path[configFolderNames.length];
        for (int i=0; i<configFolderNames.length; i++) {
            configFolderPaths[i] = Paths.get(configFolderNames[i]);
        }

        String configFolderName = "";
        for (int i=0; i<configFolderNames.length; i++) {
            if (Files.exists(configFolderPaths[i]) && Files.isDirectory(configFolderPaths[i])) {
                configFolderName = configFolderNames[i];
                break;
            }
        }

        String configFilePath = configFolderName + "/" + configFileName;
        Path configFile = Paths.get(configFilePath);
        
        ConfigHandler configHandler = new ConfigHandler(configFilePath);
        if (!Files.exists(configFile)) {
            System.err.println("Configuration file not found in "+configFilePath);
            if (!hasWritePrivileges(configFolderName)){
                System.err.println("No write privileges to "+configFolderName);
                System.err.println("Try running the program as root to write the configuration file.");
                System.exit(0);
            } else {
                System.out.println("Writing config...");
                configHandler.createConfig(configurationParameters);
            }
            System.out.println("Configuration file created at "+configFilePath);
            System.out.println("Run the program again to update you DDNS records.");
        } else {
            configHandler.readConfig();
            String username = configHandler.getProperty("username");
            String password = configHandler.getProperty("password");
            String hostname = configHandler.getProperty("hostname");
            String domain = configHandler.getProperty("domain");
            //procedes to update DDNS
            //System.out.println(username+" "+password+" "+hostname+" "+domain);
            try {
                String currentIP = DyDNS.getCurrentIP();
                DyDNS.NoIpUpdate(username, password, hostname, domain, currentIP);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    // public static void createConfig(ConfigHandler configHandler, String[] configurationParameters) {
        
    //     Scanner scanner = new Scanner(System.in);
    //     String value;
    //     for (int i=0; i<configurationParameters.length; i++){
    //         System.out.println("Enter "+configurationParameters[i]+":");
    //         value = scanner.nextLine();
    //         configHandler.setProperty(configurationParameters[i],value);
    //     }

    //     // Write to config file
    //     configHandler.writeConfig();
    // }

}
